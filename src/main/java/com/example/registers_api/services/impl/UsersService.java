package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.exceptions.ErrorWithKeycloakException;
import com.example.registers_api.services.IUserService;
import com.sun.codemodel.JCatchBlock;
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

import java.time.format.DateTimeFormatter;
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
    public String createUser(UserDTO user){
        try{
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);

            attributes.put(DOCUMENT_TYPE, Collections.singletonList(user.getIdentificationType()));
            attributes.put(DOCUMENT_NUMBER, Collections.singletonList(user.getIdentificationNumber()));
            attributes.put(RESEARCH_LAYER, Collections.singletonList(user.getResearchLayer()));
            attributes.put(BIRTHDATE, Collections.singletonList(user.getBirthDate().format(formatter)));
            newUser.setAttributes(attributes);


            Response response = usersResource.create(newUser);

            if (response.getStatus() == STATUS_CODE_201) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);

                RolesResource roleResource = keycloak.realm(REALM_NAME).roles();

                RoleRepresentation defaultRole = roleResource.get(user.getRole()).toRepresentation();

                keycloak.realm(REALM_NAME).users().get(userId).roles().realmLevel().add(Collections.singletonList(defaultRole));

                return USER_CREATED ;
            } else {
                return ERROR_WITH_USER_CREATED + response.readEntity(String.class) + response.getStatus();
            }
        } catch (Exception e) {
            throw new ErrorWithKeycloakException(ERROR_CREATING_USER);
        }
    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        try{
            UsersResource usersResource = keycloak.realm(REALM_NAME).users();
            return usersResource.list();
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

    @Override
    public UserResource getUserById(String userId) {
        try{
            UsersResource usersResource = keycloak.realm(REALM_NAME).users();
            return usersResource.get(userId);
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }
    }

    @Override
    public void deleteUser(String userId) {
        try{
            UsersResource usersResource = keycloak.realm(REALM_NAME).users();
            usersResource.get(userId).remove();
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        try{
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
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

}
