package com.example.registers_api.services.validations;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.exceptions.DoesntHavePermissions;
import com.example.registers_api.exceptions.NotEmptyFieldException;
import com.example.registers_api.exceptions.NotEnabledException;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.VariablesGroupRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.registers_api.utils.Constants.*;
import static com.example.registers_api.utils.ExceptionConstants.*;

@AllArgsConstructor
@Component
public class RegistersServiceValidations {

    private final VariableRepository variableRepository;
    private final ResearchLayerRepository layerRepository;
    private final Keycloak keycloak;

    public void validateVariablesAndResearchLayer(RegisterRequest registerRequest) {
        List<VariablesGroupRequest> variables = registerRequest.getRegisterInfo().getVariablesInfo();
        String idString = registerRequest.getRegisterInfo().getResearchLayerId();
        boolean exists = layerRepository.existsById(registerRequest.getRegisterInfo().getResearchLayerId());
        if (!exists) {
            throw new DoesntExistsException(String.format(
                    RESEARCH_LAYER_ID_NOT_FOUND, idString
            ));
        }

        for (VariablesGroupRequest variable : variables) {
            if (!variableRepository.existsById(variable.getId())) {
                throw (new DoesntExistsException(String.format(VARIABLE_ID_NOT_FOUND, variable.getId())));
            }
        }
    }

    public void validateRegisterFields(RegisterRequest registerRequest) {
        List<VariablesGroupRequest> variables = registerRequest.getRegisterInfo().getVariablesInfo();

        if (Objects.isNull(variables)) {
            throw new NotEmptyFieldException(NOT_EMPTY_VARIABLES);
        }

    }

    public void validateResearchLayer(String userEmail, RegisterRequest registerRequest) {
        List<String> userResearchLayerIds = getUserResearchLayer(userEmail);
        if (!userResearchLayerIds.contains(registerRequest.getRegisterInfo().getResearchLayerId())) {
            throw new DoesntHavePermissions(DOESNT_HAVE_PERMISSIONS);
        }
    }

    public List<String> getUserResearchLayer(String userEmail) {
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();

        List<UserRepresentation> users = usersResource.searchByEmail(userEmail, true);

        if (users.isEmpty()) {
            throw new NotFoundException(String.format(USER_NOT_FOUND_BY_EMAIL, userEmail));
        }

        UserRepresentation user = users.get(0);
        Map<String, List<String>> atributos = user.getAttributes();

        if (atributos != null && atributos.containsKey(RESEARCH_LAYER_ID)) {
            return atributos.get(RESEARCH_LAYER_ID);
        }
        else{
            throw new NotEnabledException("Los atributos del usuario son null");
        }
    }

}
