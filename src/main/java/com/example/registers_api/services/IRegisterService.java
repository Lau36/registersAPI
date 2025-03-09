package com.example.registers_api.services;

import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.PaginatedResponse;

public interface IRegisterService {
    void saveRegister(RegisterRequest register);
    PaginatedResponse getAllRegistersPaginated(PaginationRequest paginationRequest);
    void updateRegister(String registerId, RegisterRequest register);

}
