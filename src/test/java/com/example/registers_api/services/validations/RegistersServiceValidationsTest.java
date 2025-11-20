package com.example.registers_api.services.validations;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.DoesntHavePermissions;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.HealthProfessional;
import com.example.registers_api.models.Variable;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.ResearchLayerGroupRequest;
import com.example.registers_api.request.VariableRequest;
import com.example.registers_api.request.VariablesGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.registers_api.utils.Constants.REALM_NAME;
import static com.example.registers_api.utils.ExceptionConstants.DOESNT_HAVE_PERMISSIONS;
import static com.example.registers_api.utils.ExceptionConstants.NOT_EMPTY_VARIABLES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistersServiceValidationsTest {
    @Mock
    private VariableRepository variableRepository;

    @Mock
    private ResearchLayerRepository layerRepository;

    @Mock
    private Keycloak keycloak;

    @Mock
    private UsersResource usersResource;

    @InjectMocks
    private RegistersServiceValidations validations;

    private RegisterRequest registerRequest;
    private VariablesGroupRequest variableGroupRequest;
    private ResearchLayerGroupRequest researchLayerGroupRequest;

    @BeforeEach
    void setUp() {

        variableGroupRequest = new VariablesGroupRequest();
        variableGroupRequest.setId("var001");

        researchLayerGroupRequest = new ResearchLayerGroupRequest();
        researchLayerGroupRequest.setResearchLayerId("layer001");
        researchLayerGroupRequest.setResearchLayerName("name");

        registerRequest = new RegisterRequest();
        registerRequest.setRegisterInfo(researchLayerGroupRequest);
    }

    // --- validateVariablesAndResearchLayer ---

    @Test
    void validateVariablesAndResearchLayer_ShouldPass_WhenLayerAndVariablesExist() {

        VariablesGroupRequest variableGroupRequest2 = new VariablesGroupRequest();
        variableGroupRequest2.setId("var001");

        ResearchLayerGroupRequest researchLayerGroupRequest2 = new ResearchLayerGroupRequest();
        researchLayerGroupRequest2.setResearchLayerId("layer001");
        researchLayerGroupRequest2.setResearchLayerName("name");
        researchLayerGroupRequest2.setVariablesInfo(List.of(variableGroupRequest2));

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setRegisterInfo(researchLayerGroupRequest2);

        when(layerRepository.existsById(registerRequest2.getRegisterInfo().getResearchLayerId())).thenReturn(true);
        when(variableRepository.existsById(registerRequest2.getRegisterInfo().getVariablesInfo().get(0).getId())).thenReturn(true);

        validations.validateVariablesAndResearchLayer(registerRequest2);

        verify(layerRepository, times(1)).existsById("layer001");
        verify(variableRepository, times(1)).existsById("var001");
    }

    @Test
    void validateVariablesAndResearchLayer_ShouldThrow_WhenLayerNotFound() {
        when(layerRepository.existsById("layer001")).thenReturn(false);

        DoesntExistsException ex = assertThrows(
                DoesntExistsException.class,
                () -> validations.validateVariablesAndResearchLayer(registerRequest)
        );

        assertTrue(ex.getMessage().contains("No existe una capa de investigación"));
    }

    @Test
    void validateVariablesAndResearchLayer_ShouldThrow_WhenVariableNotFound() {
        VariablesGroupRequest variableGroupRequest2 = new VariablesGroupRequest();
        variableGroupRequest2.setId("var001");

        ResearchLayerGroupRequest researchLayerGroupRequest2 = new ResearchLayerGroupRequest();
        researchLayerGroupRequest2.setResearchLayerId("layer001");
        researchLayerGroupRequest2.setResearchLayerName("name");
        researchLayerGroupRequest2.setVariablesInfo(List.of(variableGroupRequest2));

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setRegisterInfo(researchLayerGroupRequest2);

        when(layerRepository.existsById("layer001")).thenReturn(true);
        when(variableRepository.existsById("var001")).thenReturn(false);

        DoesntExistsException ex = assertThrows(
                DoesntExistsException.class,
                () -> validations.validateVariablesAndResearchLayer(registerRequest2)
        );

        assertTrue(ex.getMessage().contains("No existe una variable con el id"));
    }

    // --- validateRegisterFields ---

    @Test
    void validateRegisterFields_ShouldThrow_WhenVariablesIsNull() {
        researchLayerGroupRequest.setVariablesInfo(null);

        NotEmptyFieldException ex = assertThrows(
                NotEmptyFieldException.class,
                () -> validations.validateRegisterFields(registerRequest)
        );

        assertEquals(NOT_EMPTY_VARIABLES, ex.getMessage());
    }

    @Test
    void validateRegisterFields_ShouldPass_WhenVariablesExist() {
        VariablesGroupRequest variableGroupRequest2 = new VariablesGroupRequest();
        variableGroupRequest2.setId("var001");

        ResearchLayerGroupRequest researchLayerGroupRequest2 = new ResearchLayerGroupRequest();
        researchLayerGroupRequest2.setResearchLayerId("layer001");
        researchLayerGroupRequest2.setResearchLayerName("name");
        researchLayerGroupRequest2.setVariablesInfo(List.of(variableGroupRequest2));

        RegisterRequest registerRequest2 = new RegisterRequest();
        registerRequest2.setRegisterInfo(researchLayerGroupRequest2);

        assertDoesNotThrow(() -> validations.validateRegisterFields(registerRequest2));
    }

    // --- validateResearchLayer ---

    @Test
    void validateResearchLayer_ShouldPass_WhenUserHasAccess() {
        RegistersServiceValidations spyValidations = spy(validations);
        doReturn(List.of("layer001", "layer002")).when(spyValidations).getUserResearchLayer("user@test.com");

        assertDoesNotThrow(() -> spyValidations.validateResearchLayer("user@test.com", "layer001"));
    }

    @Test
    void validateResearchLayer_ShouldThrow_WhenUserDoesNotHaveAccess() {
        RegistersServiceValidations spyValidations = spy(validations);
        doReturn(List.of("layer002")).when(spyValidations).getUserResearchLayer("user@test.com");

        DoesntHavePermissions ex = assertThrows(
                DoesntHavePermissions.class,
                () -> spyValidations.validateResearchLayer("user@test.com", "layer001")
        );

        assertEquals(DOESNT_HAVE_PERMISSIONS, ex.getMessage());
    }

    // --- getUserResearchLayer ---

//    @Test
//    void getUserResearchLayer_ShouldReturnList_WhenUserHasAttributes() {
//        when(keycloak.realm(REALM_NAME)).thenReturn(mock(org.keycloak.admin.client.resource.RealmResource.class));
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//
//        UserRepresentation user = new UserRepresentation();
//        Map<String, List<String>> attributes = new HashMap<>();
//        attributes.put(RESEARCH_LAYER_ID, List.of("layer001", "layer002"));
//        user.setAttributes(attributes);
//
//        when(usersResource.searchByEmail("user@test.com", true)).thenReturn(List.of(user));
//
//        List<String> result = validations.getUserResearchLayer("user@test.com");
//
//        assertEquals(2, result.size());
//        assertTrue(result.contains("layer001"));
//    }
//
//    @Test
//    void getUserResearchLayer_ShouldThrow_WhenUserNotFound() {
//        when(keycloak.realm(REALM_NAME)).thenReturn(mock(org.keycloak.admin.client.resource.RealmResource.class));
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//        when(usersResource.searchByEmail("user@test.com", true)).thenReturn(Collections.emptyList());
//
//        NotFoundException ex = assertThrows(
//                NotFoundException.class,
//                () -> validations.getUserResearchLayer("user@test.com")
//        );
//
//        assertTrue(ex.getMessage().contains("No se encontró el usuario con email"));
//    }
//
//    @Test
//    void getUserResearchLayer_ShouldThrow_WhenAttributesAreNull() {
//        when(keycloak.realm(REALM_NAME)).thenReturn(mock(org.keycloak.admin.client.resource.RealmResource.class));
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//
//        UserRepresentation user = new UserRepresentation();
//        user.setAttributes(null);
//
//        when(usersResource.searchByEmail("user@test.com", true)).thenReturn(List.of(user));
//
//        NotEnabledException ex = assertThrows(
//                NotEnabledException.class,
//                () -> validations.getUserResearchLayer("user@test.com")
//        );
//
//        assertEquals("Los atributos del usuario son null", ex.getMessage());
//    }
}
