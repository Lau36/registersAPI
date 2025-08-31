package com.example.registers_api.services.validations;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.exceptions.*;
import com.example.registers_api.models.ResearchLayerCollection;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.utils.ExceptionConstants;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.registers_api.utils.Constants.ERROR_WITH_KEYCLOAK;
import static com.example.registers_api.utils.Constants.REALM_NAME;
import static com.example.registers_api.utils.ExceptionConstants.USER_DOESNT_EXISTS;

@AllArgsConstructor
@Component
public class ResearchLayerServiceValidations {

    private final ResearchLayerRepository researchLayerRepository;
    private final Keycloak keycloak;

    public void notEmptyValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getLayerName().trim().isEmpty()
                || researchLayerDTO.getDescription().trim().isEmpty()
                || researchLayerDTO.getLayerBoss().getName().trim().isEmpty()
                || researchLayerDTO.getLayerBoss().getId() == 0
                || researchLayerDTO.getLayerBoss().getIdentificationNumber().trim().isEmpty()) {
            throw new NotEmptyFieldException(ExceptionConstants.NOT_EMPTY_FIELDS);
        }
    }

    public void alreadyExistsResearchLayerValidation(ResearchLayerDTO researchLayerDTO) {
        boolean exists = researchLayerRepository.existsByLayerNameAndIsEnabled(researchLayerDTO.getLayerName(), true);
        if (exists) {
            throw new AlreadyExistsException(String.format(ExceptionConstants.ALREADY_RESEARCH_LAYER_NAME_EXIST_EXCEPTION, researchLayerDTO.getLayerName()));
        }
    }

    public void alreadyExistsResearchLayerValidationUpdate(ResearchLayerDTO researchLayerDTO, ResearchLayerCollection existingResearchLayerCollection) {

        researchLayerRepository.findByLayerNameAndIsEnabled(researchLayerDTO.getLayerName(), true)
                .ifPresent(existingLayer -> {
                    if (!existingLayer.getId().equals(existingResearchLayerCollection.getId())) {
                        throw new AlreadyExistsException(
                                String.format(ExceptionConstants.ALREADY_RESEARCH_LAYER_NAME_EXIST_EXCEPTION, researchLayerDTO.getLayerName())
                        );
                    }
                });
    }

    public void tooLongValidations(ResearchLayerDTO researchLayerDTO) {
        if (researchLayerDTO.getLayerName().length() > 100) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "name capa", 100));
        } else if (researchLayerDTO.getDescription().length() > 200) {
            throw new MaxLengthExceededException(String.format(ExceptionConstants.MAX_LENGTH_EXCEEDED, "description", 200));
        }
    }

    public void validateLayerBoss(String userEmail){
        UsersResource usersResource = keycloak.realm(REALM_NAME).users();
        List<UserRepresentation> user = usersResource.searchByEmail(userEmail, true);

        if(user.isEmpty()){
            throw new UserDoesntExists(USER_DOESNT_EXISTS);
        }

    }

}
