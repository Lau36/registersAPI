package com.example.registers_api.services.impl;

import com.example.registers_api.exceptions.DoesntExistsException;
import com.example.registers_api.models.*;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.services.validations.RegistersServiceValidations;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.registers_api.utils.ExceptionConstants.REGISTER_NOT_FOUND;

@AllArgsConstructor
@Service
public class RegisterService implements IRegisterService {
    private final RegisterRepository registerRepository;
    private final RegistersServiceValidations registersServiceValidations;

    @Override
    public void saveRegister(RegisterRequest register) {

        List<Variable> variables = register.getVariables();
        Patient patient = register.getPatient();
        Caregiver caregiver = register.getCaregiver();
        HealtProfessional healtProfessional = register.getHealtProfessional();

        registersServiceValidations.validateVariablesAndResearchLayers(register);

        RegisterCollection registerCollection = RegisterCollection.builder()
                .registerDate(LocalDateTime.now())
                .variables(variables)
                .patientBasicInfo(patient)
                .caregiver(caregiver)
                .healtProfessional(healtProfessional)
                .build();

        registerRepository.save(registerCollection);
    }

    @Override
    public PaginatedResponse getAllRegistersPaginated(PaginationRequest paginationRequest) {
        Sort sort = Sort.by(Sort.Direction.fromString(paginationRequest.getSortDirection().name()), paginationRequest.getSort());
        PageRequest pageable = PageRequest.of(paginationRequest.getPage(), paginationRequest.getSize(), sort);

        long totalElements = registerRepository.count();
        int totalPages = (int) Math.ceil(totalElements / (double) paginationRequest.getSize());

        return PaginatedResponse.builder()
                .registers(registerRepository.findAllBy(pageable))
                .currentPage(paginationRequest.getPage())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    @Override
    public void updateRegister(String registerId, RegisterRequest register) {
        RegisterCollection existingRegister = registerRepository.findById(registerId)
                .orElseThrow(() -> new DoesntExistsException(
                        String.format(REGISTER_NOT_FOUND, registerId)
                ));

        registersServiceValidations.validateVariablesAndResearchLayers(register);

        existingRegister.setVariables(register.getVariables());
        existingRegister.setPatientBasicInfo(register.getPatient());
        existingRegister.setCaregiver(register.getCaregiver());
        existingRegister.setHealtProfessional(register.getHealtProfessional());
        existingRegister.setUpdateRegisterDate(LocalDateTime.now());

        registerRepository.save(existingRegister);
    }
}
