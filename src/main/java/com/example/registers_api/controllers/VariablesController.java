package com.example.registers_api.controllers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.VariablesResponse;
import com.example.registers_api.services.IVariableService;
import com.example.registers_api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Variable")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor


public class VariablesController {

    private final IVariableService variableService;

    @PostMapping
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> saveVariable(@RequestBody VariableDTO variableDTO) {
        BasicResponse response = new BasicResponse(Constants.VARIABLE_CREATED);
        variableService.saveVariable(variableDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> updateVariable(@RequestParam String variableId,
                                                        @RequestBody VariableDTO variableDTO) {
        BasicResponse response = new BasicResponse(Constants.VARIABLE_UPDATED);
        variableService.updateVariable(variableId, variableDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> deleteVariable(@RequestParam String variableId) {
        BasicResponse response = new BasicResponse(Constants.VARIABLE_DELETED);
        variableService.deleteVariable(variableId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ResearchLayerId")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "')")
    public ResponseEntity<List<VariablesResponse>> getVariablesByResearchLayerId(@RequestParam String researchLayerId) {
        return ResponseEntity.ok(variableService.getAllVariablesById(researchLayerId));
    }

    @GetMapping()
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "')")
    public ResponseEntity<VariablesResponse> getVariableById(@RequestParam String id) {
        return ResponseEntity.ok(variableService.getVariableById(id));
    }

    @GetMapping("/GetAll")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "')")
    public ResponseEntity<List<VariablesResponse>> getAllVariables() {
        return ResponseEntity.ok(variableService.getAllVariables());
    }



}
