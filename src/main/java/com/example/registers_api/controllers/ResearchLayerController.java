package com.example.registers_api.controllers;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.services.ResearchLayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ResearchLayer")
@RequiredArgsConstructor
public class ResearchLayerController {

    private final ResearchLayerService researchLayerService;

    @PostMapping
    public ResponseEntity<String> saveLayer(ResearchLayerDTO researchLayer) {
        return ResponseEntity.ok(researchLayerService.saveResearchLayer(researchLayer));
    }
}
