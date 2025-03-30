package com.example.registers_api.services;

import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;

public interface IRegisterService {
    void saveRegister(RegisterRequest register, String userEmail);
    void updateRegister(String registerId, String userEmail, RegisterRequest register);
    void deleteRegister(String registerId);

    PaginatedResponse getAllRegistersPaginated(PaginationRequest paginationRequest);
    PaginatedResponse getAllRegistersByPatientPaginated(
            PaginationRequest paginationRequest, Integer patientIdentificationNumber);

    PaginatedResponse getAllRegistersByHealthProfesionalPaginated(
            PaginationRequest paginationRequest, Integer healthProfesionalIdentificationNumber);

    PaginatedResponse getAllRegistersByResearchLayerPaginated(PaginationRequest paginationRequest, String researchLayerId);


}
