package com.example.registers_api.services;

import com.example.registers_api.dtos.AuthDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IAuthService {
    ResponseEntity<Map> login(AuthDTO auth);
    ResponseEntity<Map> refreshToken(String refreshToken);
    ResponseEntity<String> logout(String refreshToken);
}
