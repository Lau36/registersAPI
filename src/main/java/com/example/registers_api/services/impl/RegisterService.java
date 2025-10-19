package com.example.registers_api.services.impl;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.FileRespository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.VariableRequest;
import com.example.registers_api.response.*;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.registers_api.utils.Constants.*;
import static com.example.registers_api.utils.ExceptionConstants.*;

@AllArgsConstructor
@Service
public class RegisterService implements IRegisterService {
    private final RegisterRepository registerRepository;
    private final VariableRepository variableRepository;
    private final ResearchLayerRepository researchLayerRepository;
    private final RegistersServiceValidations registersServiceValidations;
    private final FileRespository fileRespository;

    @Override
    public void saveRegister(RegisterRequest register,  String userEmail) {

//        List<Variable> variables = addNamesToVariables(register.getVariables());
//        Patient patient = register.getPatient();
//        Caregiver caregiver = register.getCaregiver();
//
//        registersServiceValidations.validateResearchLayer(userEmail, register);
//        registersServiceValidations.validateRegisterFields(register);
//        registersServiceValidations.validateVariablesAndResearchLayer(register);
//
//        Map<String, ResearchLayerGroup> grouped = new LinkedHashMap<>();
//
//        register.getVariables().forEach(vReq -> {
//            String layerId = vReq.getResearchLayerId();
//            String layerName = vReq.getResearchLayerName();
//
//            grouped.computeIfAbsent(layerId, id -> ResearchLayerGroup.builder()
//                    .researchLayerId(layerId)
//                    .researchLayerName(layerName)
//                    .variables(new ArrayList<>())
//                    .build()
//            ).getVariables().add(
//                    variables.stream()
//                            .filter(v -> v.getId().equals(vReq.getId()))
//                            .findFirst()
//                            .orElse(null)
//            );
//        });
//
//        RegisterCollection registerCollection = RegisterCollection.builder()
//                .patientIdentificationNumber(register.getPatientIdentificationNumber())
//                .patientIdentificationType(register.getPatientIdentificationType())
//                .registerInfo(new ArrayList<>(grouped.values()))
//                .patientBasicInfo(patient)
//                .caregiver(caregiver)
//                .build();
//
//        registerRepository.save(registerCollection);
    }

    @Override
    public PaginatedResponse getAllRegistersPaginated(PaginationRequest paginationRequest) {
//        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
//        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
//
//        long totalElements = registerRepository.count();
//        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());
//
//        List<RegistersResponse> registers = getRegister(registerRepository.findAllBy(pageable));
//
//        return PaginatedResponse.builder()
//                .registers(registers)
//                .currentPage(paginationRequest.getPage())
//                .totalPages(totalPages)
//                .totalElements(totalElements)
//                .build();
        return null;
    }

    @Override
    public void updateRegister(String registerId, String userEmail, RegisterRequest register) {
//        RegisterCollection existingRegister = registerRepository.findById(registerId)
//                .orElseThrow(() -> new DoesntExistsException(
//                        String.format(REGISTER_NOT_FOUND, registerId)
//                ));
//
//        registersServiceValidations.validateResearchLayer(userEmail, register);
//        registersServiceValidations.validateRegisterFields(register);
//        registersServiceValidations.validateVariablesAndResearchLayer(register);
//
//        List<Variable> variables = addNamesToVariables(register.getVariables());
//
//        Map<String, ResearchLayerGroup> grouped = new LinkedHashMap<>();
//
//        register.getVariables().forEach(vReq -> {
//            String layerId = vReq.getResearchLayerId();
//            String layerName = vReq.getResearchLayerName();
//
//            grouped.computeIfAbsent(layerId, id -> ResearchLayerGroup.builder()
//                    .researchLayerId(layerId)
//                    .researchLayerName(layerName)
//                    .variables(new ArrayList<>())
//                    .build()
//            ).getVariables().add(
//                    variables.stream()
//                            .filter(v -> v.getId().equals(vReq.getId()))
//                            .findFirst()
//                            .orElse(null)
//            );
//        });
//
//        existingRegister.setRegisterInfo(new ArrayList<>(grouped.values()));
//        existingRegister.setPatientBasicInfo(register.getPatient());
//        existingRegister.setCaregiver(register.getCaregiver());
//
//        registerRepository.save(existingRegister);
    }

    @Override
    public PaginatedResponse getAllRegistersByPatientPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber) {
//        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
//        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
//
//        List<RegistersResponse> registers = getRegister(registerRepository
//                .findAllByPatientIdentificationNumber(patientIdentificationNumber, pageable));
//        long totalElements = registerRepository.countByPatientIdentificationNumber(patientIdentificationNumber);
//        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());
//
//        return PaginatedResponse.builder()
//                .registers(registers)
//                .currentPage(paginationRequest.getPage())
//                .totalPages(totalPages)
//                .totalElements(totalElements)
//                .build();
        return null;
    }

    @Override
    public PaginatedResponse getAllRegistersByHealthProfesionalPaginated(PaginationRequest paginationRequest, Integer healthProfesionalIdentificationNumber) {
        return null;
    }


    @Override
    public PaginatedResponse getAllRegistersByResearchLayerPaginated(PaginationRequest paginationRequest, String researchLayerId) {
//        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
//        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);
//
//        List<RegistersResponse> registers = getRegister(registerRepository.
//                findAllByRegisterInfoResearchLayerId(researchLayerId, pageable));
//        long totalElements = registerRepository.countByRegisterInfoResearchLayerId(researchLayerId);
//        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());
//
//        return PaginatedResponse.builder()
//                .registers(registers)
//                .currentPage(paginationRequest.getPage())
//                .totalPages(totalPages)
//                .totalElements(totalElements)
//                .build();
        return null;
    }

    @Override
    public void deleteRegister(String registerId) {
          
        registerRepository.deleteById(registerId);
    }

    public void getRegister(List<RegisterCollection> registerCollection) {
//        return registerCollection.stream()
//                .map(this::mapToResponse)
//                .toList();
    }

    public void mapToResponse(RegisterCollection register) {
//        RegistersResponse response = new RegistersResponse();
//        response.setRegisterId(register.getId());
//        response.setPatientIdentificationNumber(register.getPatientIdentificationNumber());
//        response.setPatientIdentificationType(register.getPatientIdentificationType());
//        response.setPatientBasicInfo(register.getPatientBasicInfo());
//        response.setCaregiver(register.getCaregiver());
//
//        List<ResearchLayerGroupResponse> groupResponses = new ArrayList<>();
//
//        for (ResearchLayerGroup group : register.getRegisterInfo()) {
//            ResearchLayerGroupResponse groupResponse = new ResearchLayerGroupResponse();
//            groupResponse.setResearchLayerId(group.getResearchLayerId());
//            groupResponse.setResearchLayerName(group.getResearchLayerName());
//
//            List<VariableInRegisterResponse> variableResponses = new ArrayList<>();
//
//            for (Variable variable : group.getVariables()) {
//                String variableName = variableRepository.findById(variable.getId())
//                        .map(VariableCollection::getVariableName)
//                        .orElse("Unknown");
//
//                VariableInRegisterResponse varResponse = setVariableValueInVariableResponse(variable, variableName);
//
//                variableResponses.add(varResponse);
//            }
//
//            groupResponse.setVariablesInfo(variableResponses);
//            groupResponses.add(groupResponse);
//        }
//
//        response.setRegisterInfo(groupResponses);
//        return response;
    }

    public List<Variable> addNamesToVariables(List<VariableRequest> variables){
//        List<Variable> variablesWithNames = new ArrayList<>();
//
//        for (VariableRequest var : variables) {
//            Optional<VariableCollection> variableFromDb = variableRepository.findById(var.getId());
//
//            if (variableFromDb.isEmpty()) {
//                throw new NotFoundException("Variable con ID " + var.getId() + " no encontrada");
//            }
//
//            Variable newVariable = setVariableValueInVariable(var, variableFromDb);
//
//            variablesWithNames.add(newVariable);
//        }
//        return variablesWithNames;
        return null;
    }

    public VariableInRegisterResponse  setVariableValueInVariableResponse(Variable variable, String variableName){
        VariableInRegisterResponse varResponse = new VariableInRegisterResponse();
        varResponse.setVariableId(variable.getId());
        varResponse.setVariableName(variableName);
        varResponse.setVariableType(variable.getType());

        switch (variable.getType()) {
            case NUMBER_TYPE:
                varResponse.setValueAsNumber(variable.getValueAsNumber());
                break;

            case STRING_TYPE:
                varResponse.setValueAsString(variable.getValueAsString());
                break;

            default:
                throw new IllegalArgumentException(TYPE_UNDEFINED);
        }

        return varResponse;
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

}
