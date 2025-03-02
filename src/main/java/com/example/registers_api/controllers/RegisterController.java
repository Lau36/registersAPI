package com.example.registers_api.controllers;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registers")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {

    private IRegisterService registerService;

    @PostMapping
//    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "')")
    public ResponseEntity<BasicResponse> saveRegister(@RequestBody RegisterRequest registerRequest) {
        BasicResponse response = new BasicResponse(Constants.REGISTER_CREATED);
        registerService.saveRegister(registerRequest);
        return ResponseEntity.ok(response);
    }
}
