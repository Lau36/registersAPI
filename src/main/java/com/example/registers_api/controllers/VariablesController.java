package com.example.registers_api.controllers;

import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.VariableService;
import com.example.registers_api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.util.List;

@RestController
@RequestMapping("/Variable")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VariablesController {

    private final VariableService variableService;

    @PostMapping
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> saveVariable(@RequestBody VariableDTO variableDTO) {
        BasicResponse response = new BasicResponse(Constants.VARIABLE_CREATED);
        variableService.saveVariable(variableDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ResearchLayerId")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "')")
    public ResponseEntity<List<VariableDTO>> getVariablesByResearchLayerId(@RequestParam String researchLayerId) {
        return ResponseEntity.ok(variableService.getAllVariablesById(researchLayerId));
    }

    @GetMapping()
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "')")
    public ResponseEntity<VariableDTO> getVariableById(@RequestParam String id) {
        return ResponseEntity.ok(variableService.getVariableById(id));
    }

    @GetMapping("/GetAll")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "')")
    public ResponseEntity<List<VariableDTO>> getAllResearchLayers() {
        return ResponseEntity.ok(variableService.getAllVariables());
    }

}
