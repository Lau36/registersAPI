package com.example.registers_api.controllers;

import com.example.registers_api.models.FileCollection;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.SortDirection;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.utils.Constants;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/registers")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {

    private IRegisterService registerService;

    @PostMapping()
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> saveRegister(@RequestParam String userEmail, @RequestBody RegisterRequest registerRequest) {
        BasicResponse response = new BasicResponse(Constants.REGISTER_CREATED);
        registerService.saveRegister(registerRequest, userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<PaginatedResponse> getAllRegisters(@RequestParam int page,
                                                             @RequestParam int size,
                                                             @RequestParam String sort,
                                                             @RequestParam String sortDirection) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .sortDirection(SortDirection.valueOf(sortDirection.toUpperCase()))
                .build();
        PaginatedResponse response = registerService.getAllRegistersPaginated(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping()
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> updateRegister(@RequestParam String registerId, @RequestParam String userEmail, @RequestBody RegisterRequest registerRequest) {
        BasicResponse response = new BasicResponse(Constants.REGISTER_UPDATED);
        registerService.updateRegister(registerId, userEmail, registerRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> deleteRegisterById(@RequestParam String registerId) {
        BasicResponse response = new BasicResponse(Constants.REGISTER_DELETED);
        registerService.deleteRegister(registerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allByPatient")
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<PaginatedResponse> getAllRegistersByPatient(
              @RequestParam int patientIdentificationNumber,
              @RequestParam int page,
              @RequestParam int size,
              @RequestParam String sort,
              @RequestParam String sortDirection) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .sortDirection(SortDirection.valueOf(sortDirection.toUpperCase()))
                .build();
        PaginatedResponse response = registerService.getAllRegistersByPatientPaginated(request, patientIdentificationNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allByHealtProfessional")
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<PaginatedResponse> getAllRegistersByHealthProfesional(
            @RequestParam int healthProfesionalIdentificationNumber,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam String sortDirection) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .sortDirection(SortDirection.valueOf(sortDirection.toUpperCase()))
                .build();
        PaginatedResponse response = registerService.getAllRegistersByHealthProfesionalPaginated(request, healthProfesionalIdentificationNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allByResearchLayer")
    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<PaginatedResponse> getAllRegistersByResearchLayer(
            @RequestParam String researchLayerId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam String sortDirection) {
        PaginationRequest request = PaginationRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .sortDirection(SortDirection.valueOf(sortDirection.toUpperCase()))
                .build();
        PaginatedResponse response = registerService.getAllRegistersByResearchLayerPaginated(request, researchLayerId);
        return ResponseEntity.ok(response);
    }
}
