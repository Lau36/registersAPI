package com.example.registers_api.services;

import com.example.registers_api.request.RegisterRequest;

public interface IRegisterService {
    void saveRegister(RegisterRequest register);
}
