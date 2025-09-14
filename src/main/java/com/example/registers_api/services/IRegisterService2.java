package com.example.registers_api.services;

import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.response.RegisterResponse2;

public interface IRegisterService2 {
    void saveRegister(RegisterRequest register, String userEmail);
    void updateRegister(String registerId, RegisterRequest register, String userEmail);
    RegisterResponse2 actualPatientRegisterInfo(Integer patientIdentificationNumber);

    PaginatedResponse getAllRegistersByResearchLayerPaginated(PaginationRequest paginationRequest, String researchLayerId
            , Integer patientIdentificationNumber);
    PaginatedResponse getAllCaregiverRegistersPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber);
    PaginatedResponse getAllPatientBasicInfoRegistersPaginated(PaginationRequest paginationRequest, Integer patientIdentificationNumber);
}
