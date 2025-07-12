package com.example.registers_api.services.impl;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.FileRespository;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.repository.ResearchLayerRepository;
import com.example.registers_api.repository.VariableRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.response.RegistersResponse;
import com.example.registers_api.response.VariableResponse;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.registers_api.utils.ExceptionConstants.REGISTER_NOT_FOUND;

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

        List<Variable> variables = register.getVariables();
        Patient patient = register.getPatient();
        Caregiver caregiver = register.getCaregiver();
        HealthProfessional healthProfessional = register.getHealthProfessional();

        registersServiceValidations.validateResearchLayer(userEmail, register);
        registersServiceValidations.validateRegisterFields(register);
        registersServiceValidations.validateVariablesAndResearchLayer(register);

        RegisterCollection registerCollection = RegisterCollection.builder()
                .registerDate(LocalDateTime.now())
                .patientIdentificationNumber(register.getPatientIdentificationNumber())
                .patientIdentificationType(register.getPatientIdentificationType())
                .variables(variables)
                .patientBasicInfo(patient)
                .caregiver(caregiver)
                .healthProfessional(healthProfessional)
                .build();

        registerRepository.save(registerCollection);
    }

    @Override
    public PaginatedResponse getAllRegistersPaginated(PaginationRequest paginationRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

        long totalElements = registerRepository.count();
        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());

        List<RegistersResponse> registers = getRegister(registerRepository.findAllBy(pageable));

        return PaginatedResponse.builder()
                .registers(registers)
                .currentPage(paginationRequest.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    @Override
    public void updateRegister(String registerId, String userEmail, RegisterRequest register) {
        RegisterCollection existingRegister = registerRepository.findById(registerId)
                .orElseThrow(() -> new DoesntExistsException(
                        String.format(REGISTER_NOT_FOUND, registerId)
                ));

        registersServiceValidations.validateResearchLayer(userEmail, register);
        registersServiceValidations.validateRegisterFields(register);
        registersServiceValidations.validateVariablesAndResearchLayer(register);

        existingRegister.setVariables(register.getVariables());
        existingRegister.setPatientBasicInfo(register.getPatient());
        existingRegister.setCaregiver(register.getCaregiver());
        existingRegister.setHealthProfessional(register.getHealthProfessional());
        existingRegister.setUpdateRegisterDate(LocalDateTime.now());

        registerRepository.save(existingRegister);
    }

    @Override
    public PaginatedResponse getAllRegistersByPatientPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

        List<RegistersResponse> registers = getRegister(registerRepository
                .findAllByPatientIdentificationNumber(patientIdentificationNumber, pageable));
        long totalElements = registerRepository.countByPatientIdentificationNumber(patientIdentificationNumber);
        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());

        return PaginatedResponse.builder()
                .registers(registers)
                .currentPage(paginationRequest.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    @Override
    public PaginatedResponse getAllRegistersByHealthProfesionalPaginated(PaginationRequest paginationRequest, Integer healthProfesionalIdentificationNumber) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

        List<RegistersResponse> registers = getRegister(registerRepository.
                findAllByHealthProfessionalIdentificationNumber(healthProfesionalIdentificationNumber, pageable));
        long totalElements = registerRepository.countByHealthProfessionalIdentificationNumber(healthProfesionalIdentificationNumber);
        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());

        return PaginatedResponse.builder()
                .registers(registers)
                .currentPage(paginationRequest.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    @Override
    public PaginatedResponse getAllRegistersByResearchLayerPaginated(PaginationRequest paginationRequest, String researchLayerId) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

        List<RegistersResponse> registers = getRegister(registerRepository.
                findAllByVariablesResearchLayerId(researchLayerId, pageable));
        long totalElements = registerRepository.countByVariablesResearchLayerId(researchLayerId);
        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());

        return PaginatedResponse.builder()
                .registers(registers)
                .currentPage(paginationRequest.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }



    @Override
    public void deleteRegister(String registerId) {
        registerRepository.findById(registerId).orElseThrow(() -> new DoesntExistsException(
                String.format(REGISTER_NOT_FOUND, registerId)
        ));
        registerRepository.deleteById(registerId);
    }

    public List<RegistersResponse> getRegister(List<RegisterCollection> registerCollection) {
        return registerCollection.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RegistersResponse mapToResponse(RegisterCollection register) {
        RegistersResponse response = new RegistersResponse();
        response.setRegisterId(register.getId());
        response.setRegisterDate(register.getRegisterDate());
        response.setUpdateRegisterDate(register.getUpdateRegisterDate());
        response.setPatientIdentificationNumber(register.getPatientIdentificationNumber());
        response.setPatientIdentificationType(register.getPatientIdentificationType());
        response.setPatientBasicInfo(register.getPatientBasicInfo());
        response.setCaregiver(register.getCaregiver());
        response.setHealthProfessional(register.getHealthProfessional());

        List<VariableResponse> variableResponses = register.getVariables().stream().map(variable -> {
            String variableName = variableRepository.findById(variable.getId())
                    .map(VariableCollection::getVariableName)
                    .orElse("Unknown");

            String researchLayerName = researchLayerRepository.findById(variable.getResearchLayerId())
                    .map(ResearchLayerCollection::getLayerName)
                    .orElse("Unknown");

            return new VariableResponse(
                    variable.getId(),
                    variableName,
                    variable.getValue(),
                    variable.getType(),
                    variable.getResearchLayerId(),
                    researchLayerName
            );
        }).toList();

        response.setVariablesRegister(variableResponses);
        return response;
    }

    @Override
    public void saveFile(FileCollection file) {
        fileRespository.save(file);
    }
}
