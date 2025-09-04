package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.exceptions.ErrorUserCreation;
import com.example.registers_api.exceptions.ErrorWithKeycloakException;
import com.example.registers_api.services.IUserService;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.registers_api.utils.Constants.*;


@Service
@AllArgsConstructor
@Slf4j
public class UsersService implements IUserService {

    private final Keycloak keycloak;

    @Override
    public String createUser(@NonNull UserDTO user){
            if (!identificationNumberExists(user.getIdentificationNumber())) {
                throw new ErrorWithKeycloakException("El número de identificación ya está en uso.");
            }

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

            attributes.put(IDENTIFICATION_TYPE, Collections.singletonList(user.getIdentificationType()));
            attributes.put(IDENTIFICATION_NUMBER, Collections.singletonList(user.getIdentificationNumber().toString()));
            attributes.put(ACCEPT_TERMS_CONDITION, Collections.singletonList(user.getAcceptTermsAndConditions().toString()));
            attributes.put(RESEARCH_LAYER, user.getResearchLayer());
            attributes.put(BIRTHDATE, Collections.singletonList(user.getBirthDate().format(formatter)));
            attributes.put(ROLE, Collections.singletonList(user.getRole()));
            newUser.setAttributes(attributes);


            Response response = usersResource.create(newUser);

            if (response.getStatus() == STATUS_CODE_201) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf("/") + 1);

                RolesResource roleResource = keycloak.realm(REALM_NAME).roles();

                RoleRepresentation defaultRole = roleResource.get(user.getRole()).toRepresentation();

                keycloak.realm(REALM_NAME).users().get(userId).roles().realmLevel().add(Collections.singletonList(defaultRole));

                return USER_CREATED;
            } else {

                String message = getErrorMessage(response.readEntity(String.class));
                throw new ErrorUserCreation(message, response.getStatus());
            }

    }


    private boolean identificationNumberExists(Integer identificationNumber) {
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
                .users()
                .search(null, null, null, identificationNumber.toString(), 0, 1);

        return users.isEmpty();
    }

    public String getErrorMessage(String jsonError){


        Pattern pattern = Pattern.compile("\"errorMessage\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(jsonError);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Error desconocido";
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
    public List<UserRepresentation> getUserByEmail(String email) {
        try{
            UsersResource usersResource = keycloak.realm(REALM_NAME).users();
            return usersResource.searchByEmail(email, true);
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }
    }

    @Override
    public void deleteUser(String userId) {
        try{
            UsersResource usersResource = keycloak.realm(REALM_NAME).users();
            UserResource userResource = usersResource.get(userId);

            System.out.println("✅ usersResource: " + usersResource);
            System.out.println("✅ userResource: " + userResource);

            if (userResource == null) {
                throw new RuntimeException("❌ userResource es null");
            }

            userResource.remove();
            System.out.println("✅ Usuario eliminado exitosamente");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

    @Override
    public void disableOrEnableUser(String userId, boolean isEnabled) {
        try{
            UserRepresentation user = new UserRepresentation();
            user.setEnabled(isEnabled);
            UserResource usersResource = keycloak.realm(REALM_NAME).users().get(userId);
            usersResource.update(user);
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

    @Override
    public void updateUser(String userId, UserDTO userDTO) {
        try{

            UserRepresentation user = setUsersAtributes(userDTO, userDTO.getPassword().isEmpty());
            updateRole(userId, userDTO.getRole());

            UserResource userResource = keycloak.realm(REALM_NAME).users().get(userId);

            userResource.update(user);
        }
        catch (Exception e){
            throw new ErrorWithKeycloakException(ERROR_WITH_KEYCLOAK);
        }

    }

    public CredentialRepresentation setUserCredentials(UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        return credentialRepresentation;
    }

    public UserRepresentation setUsersAtributes(UserDTO userDTO, boolean isPasswordFieldEmpty) {
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);

        Map<String, List<String>> attributes = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE);

        if(!isPasswordFieldEmpty){
            CredentialRepresentation credentials = setUserCredentials(userDTO);
            user.setCredentials(Collections.singletonList(credentials));
            attributes.put(LAST_PASSWORD_UPDATE, Collections.singletonList(LocalDateTime.now().toString()));
        }

        attributes.put(IDENTIFICATION_TYPE, Collections.singletonList(userDTO.getIdentificationType()));
        attributes.put(IDENTIFICATION_NUMBER, Collections.singletonList(userDTO.getIdentificationNumber().toString()));
        attributes.put(RESEARCH_LAYER, userDTO.getResearchLayer());
        attributes.put(BIRTHDATE, Collections.singletonList(userDTO.getBirthDate().format(formatter)));
        attributes.put(ROLE, Collections.singletonList(userDTO.getRole()));
        user.setAttributes(attributes);

        return user;
    }

    public void updateRole(String userId, String newRoleName) {
        final String DEFAULT_ROLE = "default-roles-registeusersapidev";

        UserResource userResource = keycloak.realm(REALM_NAME).users().get(userId);
        RoleMappingResource roleMappingResource = userResource.roles();

        List<RoleRepresentation> currentRoles = roleMappingResource.realmLevel().listAll();

        RoleRepresentation currentCustomRole = currentRoles.stream()
                .filter(role -> !role.getName().equals(DEFAULT_ROLE))
                .findFirst()
                .orElse(null);

        if (currentCustomRole == null || !currentCustomRole.getName().equals(newRoleName)) {
            if (currentCustomRole != null) {
                roleMappingResource.realmLevel().remove(Collections.singletonList(currentCustomRole));
            }
            RoleRepresentation newRole = keycloak.realm(REALM_NAME)
                    .roles().get(newRoleName).toRepresentation();

            roleMappingResource.realmLevel().add(Collections.singletonList(newRole));
        }
    }
}
