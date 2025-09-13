package com.example.registers_api.services.impl;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.mappers.RegisterMapper;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.RegisterHistoryRepository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.InfoChanged;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.VariableRequest;
import com.example.registers_api.response.RegisterResponse2;
import com.example.registers_api.response.ResearchLayerGroupResponse;
import com.example.registers_api.response.VariableInRegisterResponse;
import com.example.registers_api.services.AnalyticsPipelineService;
import com.example.registers_api.services.IRegisterService2;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.registers_api.utils.Constants.*;
import static com.example.registers_api.utils.ExceptionConstants.*;

@Component
@AllArgsConstructor
public class RegisterService2 implements IRegisterService2 {
    private RegisterRepository registerRepository;
    private RegisterHistoryRepository registerHistoryRepository;
    private VariableRepository variableRepository;
    private RegistersServiceValidations registersServiceValidations;
    private AnalyticsPipelineService analyticsPipelineService;
    private RegisterMapper registerMapper;

    @Override
    public void saveRegister(RegisterRequest register, String userEmail) {

        registersServiceValidations.validateResearchLayer(userEmail, register);
        registersServiceValidations.validateRegisterFields(register);
        registersServiceValidations.validateVariablesAndResearchLayer(register);

        RegisterCollection registerCollection = RegisterCollection.builder()
                .patientIdentificationNumber(register.getPatientIdentificationNumber())
                .patientIdentificationType(register.getPatientIdentificationType())
                .registerInfo(register.getRegisterInfo())
                .patientBasicInfo(register.getPatient())
                .caregiver(register.getCaregiver())
                .build();

        registerRepository.save(registerCollection);

        RegisterCollection saved = registerRepository.save(registerCollection);
        saveFirstRegisterInRegisterHistory(saved, userEmail);
        //analyticsPipelineService.CreateCollection();
    }

    @Override
    public void updateRegister(String registerId, RegisterRequest registerRequest, String userEmail) {
        RegisterCollection existingRegister = registerRepository.findById(registerId)
                .orElseThrow(() -> new DoesntExistsException(
                        String.format(REGISTER_NOT_FOUND, registerId)
                ));

        registersServiceValidations.validateResearchLayer(userEmail, registerRequest);
        registersServiceValidations.validateRegisterFields(registerRequest);
        registersServiceValidations.validateVariablesAndResearchLayer(registerRequest);

        updateAndSaveHistory(registerRequest, userEmail, existingRegister);

    }

    @Override
    public RegisterResponse2 actualPatientRegisterInfo(Integer patientIdentificationNumber) {
        RegisterCollection collection = registerRepository.findByPatientIdentificationNumber(patientIdentificationNumber);
        return registerMapper.toRegisterResponse(collection);
    }



    public Variable setVariableValueInVariable(VariableRequest var, Optional<VariableCollection> variableFromDb){
        Variable variable = new Variable();
        variable.setId(var.getId());
        variable.setName(variableFromDb.get().getVariableName());
        variable.setType(var.getType());

        switch (var.getType()) {
            case NUMBER_TYPE:
                if (!(var.getValue() instanceof Number)) {
                    throw new IllegalArgumentException(VALUE_MUST_BE_NUMBER);
                }
                variable.setValueAsNumber(((Number) var.getValue()).doubleValue());
                break;

            case STRING_TYPE:
                if (!(var.getValue() instanceof String)) {
                    throw new IllegalArgumentException(VALUE_MUST_BE_STRING);
                }
                variable.setValueAsString((String) var.getValue());
                break;

            default:
                throw new IllegalArgumentException(TYPE_UNDEFINED);
        }

        return variable;
    }

    public void updateAndSaveHistory(RegisterRequest registerRequest,
                                     String userEmail,
                                     RegisterCollection existingRegister) {

        if(Boolean.TRUE.equals(registerRequest.getInfoChanged().getPatientBasicInfo())){
            existingRegister.setPatientBasicInfo(registerRequest.getPatient());
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_PATIENT_BASIC_INFO, registerRequest);
        }
        if(Boolean.TRUE.equals(registerRequest.getInfoChanged().getCaregiver())){
            existingRegister.setCaregiver(registerRequest.getCaregiver());
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_CAREGIVER, registerRequest);
        }
        if(Boolean.TRUE.equals(registerRequest.getInfoChanged().getRegisterInfo())){
            //Iniciar por acá para terminar el update de registers
            //Para tener en cuenta -> que si la capa de investigación no existe en la lista de registersInfo,
            // añadirla y si existe reemplazarla por la que viene en el reques
            addInRegisterHistory(existingRegister.getId(),
                    userEmail, UPDATE_RESEARCH_LAYER, registerRequest);

        }


    }

    public void saveFirstRegisterInRegisterHistory(RegisterCollection registerSaved, String userEmail) {

        RegistersHistoryCollection history = RegistersHistoryCollection.builder()
                .registerId(registerSaved.getId())
                .changedBy(userEmail)
                .changedAt(LocalDateTime.now().toString())
                .operation(REGISTER_CREATED)
                .isResearchLayerGroup(registerSaved.getRegisterInfo().get(0))
                .isPatientBasicInfo(registerSaved.getPatientBasicInfo())
                .isCaregiverInfo(registerSaved.getCaregiver())
                .build();

        registerHistoryRepository.save(history);
    }

    public void addInRegisterHistory(String registerId, String userEmail, String operation,
                                                   RegisterRequest registerRequest) {
        RegistersHistoryCollection register = RegistersHistoryCollection.builder()
                .registerId(registerId)
                .changedBy(userEmail)
                .operation(operation)
                .changedAt(LocalDateTime.now().toString())
                .build();

        switch (operation) {
            case UPDATE_RESEARCH_LAYER:
                register.setIsResearchLayerGroup(registerRequest.getRegisterInfo().get(0));
                break;

            case UPDATE_PATIENT_BASIC_INFO:
                register.setIsPatientBasicInfo(registerRequest.getPatient());
                break;

            case UPDATE_CAREGIVER:
                register.setIsCaregiverInfo(registerRequest.getCaregiver());
                break;

            default:
                throw new IllegalArgumentException(TYPE_UNDEFINED);
        }
        registerHistoryRepository.save(register);
    }

}
