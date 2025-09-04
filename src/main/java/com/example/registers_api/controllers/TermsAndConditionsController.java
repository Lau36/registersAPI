package com.example.registers_api.controllers;

import com.example.registers_api.response.TermsConditionsResponse;
import com.example.registers_api.services.ITermsAndConditions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/TermsConditions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TermsAndConditionsController {

    private final ITermsAndConditions termsAndConditions;

    @GetMapping("/GetAll")
    public ResponseEntity<TermsConditionsResponse> getAllResearchLayers() {
        return ResponseEntity.ok(termsAndConditions.getTermsAndConditions());
    }
}
