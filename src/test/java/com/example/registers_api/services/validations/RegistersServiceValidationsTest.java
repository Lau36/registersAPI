package com.example.registers_api.services.validations;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.DoesntHavePermissions;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.models.HealthProfessional;
import com.example.registers_api.models.Variable;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.VariableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistersServiceValidationsTest {

    @Mock
    private VariableRepository variableRepository;

    @Mock
    private ResearchLayerRepository researchLayerRepository;

    @Mock
    private RegisterRequest registerRequest;

    @Mock
    private VariableRequest variable;

    @InjectMocks
    private RegistersServiceValidations registersServiceValidations;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testValidateVariablesAndResearchLayer_Valid() {
        when(registerRequest.getVariables()).thenReturn(List.of(variable));
        when(variable.getResearchLayerId()).thenReturn("rl1");
        when(variable.getId()).thenReturn("var1");
        when(researchLayerRepository.existsById("rl1")).thenReturn(true);
        when(variableRepository.existsById("var1")).thenReturn(true);

        assertDoesNotThrow(() -> registersServiceValidations.validateVariablesAndResearchLayer(registerRequest));
    }

    @Test
    void testValidateVariablesAndResearchLayer_InvalidResearchLayer() {
        when(registerRequest.getVariables()).thenReturn(List.of(variable));
        when(variable.getResearchLayerId()).thenReturn("rl1");
        when(researchLayerRepository.existsById("rl1")).thenReturn(false);

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () ->
                registersServiceValidations.validateVariablesAndResearchLayer(registerRequest)
        );
        assertEquals("No existe una capa de investigación con el id: 'rl1'", exception.getMessage());
    }

    @Test
    void testValidateVariablesAndResearchLayer_InvalidVariable() {
        when(registerRequest.getVariables()).thenReturn(List.of(variable));
        when(variable.getResearchLayerId()).thenReturn("rl1");
        when(variable.getId()).thenReturn("var1");
        when(researchLayerRepository.existsById("rl1")).thenReturn(true);
        when(variableRepository.existsById("var1")).thenReturn(false);

        DoesntExistsException exception = assertThrows(DoesntExistsException.class, () ->
                registersServiceValidations.validateVariablesAndResearchLayer(registerRequest)
        );
        assertEquals("No existe una variable con el id: 'var1'", exception.getMessage());
    }

    @Test
    void testValidateRegisterFields_Valid() {
        RegisterRequest validRequest = mock(RegisterRequest.class);
        HealthProfessional healthProfessional = mock(HealthProfessional.class);
        when(validRequest.getVariables()).thenReturn(List.of(new VariableRequest()));
        when(validRequest.getHealthProfessional()).thenReturn(healthProfessional);

        assertDoesNotThrow(() -> registersServiceValidations.validateRegisterFields(validRequest));
    }

    @Test
    void testValidateRegisterFields_InvalidVariables() {
        RegisterRequest invalidRequest = mock(RegisterRequest.class);
        when(invalidRequest.getVariables()).thenReturn(null);

        NotEmptyFieldException exception = assertThrows(NotEmptyFieldException.class, () ->
                registersServiceValidations.validateRegisterFields(invalidRequest)
        );
        assertEquals("El campo de profesional de salud no puede estar vacio o con campos nulos", exception.getMessage());
    }

    @Test
    void testValidateRegisterFields_InvalidHealthProfessional() {
        RegisterRequest invalidRequest = mock(RegisterRequest.class);
        when(invalidRequest.getVariables()).thenReturn(List.of(new VariableRequest()));
        when(invalidRequest.getHealthProfessional()).thenReturn(null);

        NotEmptyFieldException exception = assertThrows(NotEmptyFieldException.class, () ->
                registersServiceValidations.validateRegisterFields(invalidRequest)
        );
        assertEquals("Las variables no pueden estar vacias o con campos nulos", exception.getMessage());
    }

//    @Test
//    void testValidateResearchLayer_Valid() {
//        String userEmail = "test@domain.com";
//        RegisterRequest validRequest = mock(RegisterRequest.class);
//        when(validRequest.getVariables()).thenReturn(List.of(variable));
//        when(variable.getResearchLayerId()).thenReturn("rl1");
//        when(registersServiceValidations.getUserResearchLayer(userEmail)).thenReturn("rl1");
//
//        assertDoesNotThrow(() -> registersServiceValidations.validateResearchLayer(userEmail, validRequest));
//    }
//
//    @Test
//    void testValidateResearchLayer_Invalid() {
//        String userEmail = "test@domain.com";
//        RegisterRequest invalidRequest = mock(RegisterRequest.class);
//        when(invalidRequest.getVariables()).thenReturn(List.of(variable));
//        when(variable.getResearchLayerId()).thenReturn("rl1");
//        when(registersServiceValidations.getUserResearchLayer(userEmail)).thenReturn("rl2");
//
//        DoesntHavePermissions exception = assertThrows(DoesntHavePermissions.class, () ->
//                registersServiceValidations.validateResearchLayer(userEmail, invalidRequest)
//        );
//        assertEquals("User does not have permission for research layer", exception.getMessage());
//    }
//
//    @Test
//    void testGetUserResearchLayer_Valid() {
//        // Arrange
//        String userEmail = "test@domain.com";
//        UserRepresentation userRepresentation = mock(UserRepresentation.class);
//        UsersResource usersResource = mock(UsersResource.class);
//        Map<String, List<String>> attributes = Map.of("RESEARCH_LAYER", List.of("rl1"));
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//        when(usersResource.searchByEmail(userEmail, true)).thenReturn(List.of(userRepresentation));
//        when(userRepresentation.getAttributes()).thenReturn(attributes);
//
//        // Act
//        String researchLayerId = registersServiceValidations.getUserResearchLayer(userEmail);
//
//        // Assert
//        assertEquals("rl1", researchLayerId);
//    }
//
//    @Test
//    void testGetUserResearchLayer_UserNotFound() {
//        // Arrange
//        String userEmail = "test@domain.com";
//        UsersResource usersResource = mock(UsersResource.class);
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//        when(usersResource.searchByEmail(userEmail, true)).thenReturn(List.of());
//
//        // Act & Assert
//        NotFoundException exception = assertThrows(NotFoundException.class, () ->
//                registersServiceValidations.getUserResearchLayer(userEmail)
//        );
//        assertEquals("User with email test@domain.com not found", exception.getMessage());
//    }
//
//    @Test
//    void testGetUserResearchLayer_AttributeNotEnabled() {
//        // Arrange
//        String userEmail = "test@domain.com";
//        UserRepresentation userRepresentation = mock(UserRepresentation.class);
//        UsersResource usersResource = mock(UsersResource.class);
//        when(keycloak.realm(REALM_NAME).users()).thenReturn(usersResource);
//        when(usersResource.searchByEmail(userEmail, true)).thenReturn(List.of(userRepresentation));
//        when(userRepresentation.getAttributes()).thenReturn(null);
//
//        // Act & Assert
//        NotEnabledException exception = assertThrows(NotEnabledException.class, () ->
//                registersServiceValidations.getUserResearchLayer(userEmail)
//        );
//        assertEquals("User attributes are null", exception.getMessage());
//    }
}
