package com.example.registers_api.services;

import com.example.registers_api.request.RegisterRequest;

public interface IRegisterService2 {
    void saveRegister(RegisterRequest register, String userEmail);
}
