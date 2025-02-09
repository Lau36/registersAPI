package com.example.registers_api.controllers;

import com.example.registers_api.dtos.AuthDTO;
import com.example.registers_api.services.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody AuthDTO loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("refresh-token") String token) {
        return authService.logout(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map> refreshToken(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
