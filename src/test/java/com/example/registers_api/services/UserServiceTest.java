package com.example.registers_api.services;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.exceptions.ErrorWithKeycloakException;
import com.example.registers_api.services.impl.UsersService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.LocalDate;

import static com.example.registers_api.utils.Constants.STATUS_CODE_201;
import static com.example.registers_api.utils.Constants.USER_CREATED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
//
//    @Mock
//    private Keycloak keycloak;
//
//    @Mock
//    private RealmResource realmResource;
//
//    @Mock
//    private UsersResource usersResource;
//
//    @Mock
//    private UserResource userResource;
//
//    @Mock
//    private RolesResource rolesResource;
//
//    @Mock
//    private Response response;
//
//    @Mock
//    private RoleResource roleResource;
//
//    @InjectMocks
//    private UsersService usersService;
//
//    private static final String REALM_NAME = "myRealm";
//    private static final String USER_ID = "12345";
//    private static final String ERROR_WITH_KEYCLOAK = "Error con servicio externo";
//
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.openMocks(this);
//
//        System.out.println("✅ keycloak: " + keycloak);
//        lenient().when(keycloak.realm(REALM_NAME)).thenReturn(realmResource);
//        lenient().when(realmResource.users()).thenReturn(usersResource);
//        lenient().when(usersResource.get(USER_ID)).thenReturn(userResource);
//        lenient().doNothing().when(userResource).remove();
//
//
//    }
//
//    @Test
//    void createUser_ShouldReturnSuccessMessage_WhenUserIsCreatedSuccessfully() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername("testUser");
//        userDTO.setEmail("test@example.com");
//        userDTO.setFirstName("Test");
//        userDTO.setLastName("User");
//        userDTO.setPassword("password");
//        userDTO.setIdentificationType("ID");
//        userDTO.setIdentificationNumber(12345);
//        userDTO.setResearchLayer("Layer1");
//        userDTO.setBirthDate(LocalDate.of(2000, 1, 1));
//        userDTO.setRole("userRole");
//
//        when(keycloak.realm(anyString())).thenReturn(realmResource);
//        when(realmResource.users()).thenReturn(usersResource);
//        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
//        when(response.getStatus()).thenReturn(STATUS_CODE_201);
//
//        // Usa lenient() si es necesario
//        lenient().when(realmResource.roles()).thenReturn(rolesResource);
//        lenient().when(rolesResource.get(anyString())).thenReturn(roleResource);
//        lenient().when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());
//
//        String result = usersService.createUser(userDTO);
//
//        assertEquals(USER_CREATED, result);
//        verify(usersResource, times(1)).create(any(UserRepresentation.class));
//        verify(rolesResource, times(1)).get(eq("userRole"));
//    }
//
//    @Test
//    void deleteUser() {
//        when(keycloak.realm(REALM_NAME)).thenReturn(realmResource);
//
//        System.out.println("✅ keycloak.realm: " + keycloak.realm(REALM_NAME));
//
//        when(realmResource.users()).thenReturn(usersResource);
//        when(usersResource.get(USER_ID)).thenReturn(userResource);
//        doNothing().when(userResource).remove();
//
//        usersService.deleteUser(USER_ID);
//    }
//
//    @Test
//    void deleteUser_ThrowsException() {
//
//        ErrorWithKeycloakException exception = assertThrows(ErrorWithKeycloakException.class, () -> usersService.deleteUser(USER_ID));
//        assertEquals(ERROR_WITH_KEYCLOAK, exception.getMessage());
//    }
}
