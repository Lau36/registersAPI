package com.example.registers_api.services.Impl;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.services.IUserService;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.registers_api.utils.Constants.*;


@Service
@AllArgsConstructor
@Slf4j
public class UsersService implements IUserService {

    private final Keycloak keycloak;

    @Override
    public String createAdmin(UserDTO user){
        return saveUser(user, ADMIN);
    }

    @Override
    public String createDoctor(UserDTO user){
        return saveUser(user, DOCTOR);
    }

    @Override
    public String createReseacher(UserDTO user){
        return saveUser(user, RESEARCHER);
    }

    public String saveUser(UserDTO user, String role) {

        UsersResource usersResource = keycloak.realm(REALM_NAME).users();

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(user.getPassword());
        credential.setTemporary(false);

        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEnabled(true);
        newUser.setEmailVerified(true);
        newUser.setCredentials(Collections.singletonList(credential));

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("IdentificationType", Collections.singletonList(user.getIdentificationType()));
        attributes.put("identificationNumber", Collections.singletonList(user.getIdentificationNumber()));
        newUser.setAttributes(attributes);

        // Crear usuario en Keycloak
        Response response = usersResource.create(newUser);

        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            // Obtener el recurso de roles del realm
            RolesResource roleResource = keycloak.realm(REALM_NAME).roles();

            // Obtener el rol por nombre
            RoleRepresentation defaultRole = roleResource.get(role).toRepresentation();

            // Asignar el rol al usuario recién creado
            keycloak.realm(REALM_NAME).users().get(userId).roles().realmLevel().add(Collections.singletonList(defaultRole));
            return "Usuario creado exitosamente" ;
        } else {
            return "Error al crear usuario: " + response.readEntity(String.class) + response.getStatus();
        }

    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();
        return usersResource.list();
    }

    @Override
    public void deleteUser(String userId) {
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();
        usersResource.get(userId).remove();
    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = keycloak.realm(REALM_NAME).users().get(userId);
        usersResource.update(user);
    }

}
