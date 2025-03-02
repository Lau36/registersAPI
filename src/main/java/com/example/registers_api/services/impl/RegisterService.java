package com.example.registers_api.services.impl;

import com.example.registers_api.models.*;
import com.example.registers_api.repository.RegisterRepository;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.services.IRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class RegisterService implements IRegisterService {
    private final RegisterRepository registerRepository;

    @Override
    public void saveRegister(RegisterRequest register) {

        List<Variable> variables = register.getVariables();
        Patient patient = register.getPatient();
        Caregiver caregiver = register.getCaregiver();
        HealtProfessional healtProfessional = register.getHealtProfessional();

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
}
