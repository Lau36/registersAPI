package com.example.registers_api.controllers;

import com.example.registers_api.dtos.ResearchLayerDTO;
import com.example.registers_api.dtos.VariableDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.VariableService;
import com.example.registers_api.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Variable")
@RequiredArgsConstructor
public class VariablesController {

    private final VariableService variableService;

    @PostMapping
    public ResponseEntity<BasicResponse> saveLayer(@RequestBody VariableDTO variableDTO) {
        BasicResponse response = new BasicResponse(Constants.VARIABLE_CREATED);
        variableService.saveVariable(variableDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<VariableDTO> getResearchLayerById(@RequestParam String id) {
        return ResponseEntity.ok(variableService.getVariableById(id));
    }

    @GetMapping("/GetAll")
    public ResponseEntity<List<VariableDTO>> getAllResearchLayers() {
        return ResponseEntity.ok(variableService.getAllVariables());
    }
}
