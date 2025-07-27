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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    private IRegisterService registerService;

    @PostMapping("/uploadDocument")
    public ResponseEntity<String> uploadDocument(@RequestParam("patientIdentification") int patientIdentification, @RequestParam("file") MultipartFile archivo) {
        try {
            FileCollection document = new FileCollection();
            document.setIdentifyPatient(patientIdentification);
            document.setTipoMime(archivo.getContentType());
            document.setContenido(new Binary(archivo.getBytes()));

            registerService.saveFile(document);

            return ResponseEntity.ok("Archivo guardado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el archivo");
        }
    }

//    @GetMapping()
//    @PreAuthorize("hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
//    public ResponseEntity<PaginatedResponse> getAllRegisters(@RequestParam int page,
//                                                             @RequestParam int size,
//                                                             @RequestParam String sort,
//                                                             @RequestParam String sortDirection) {
//        PaginationRequest request = PaginationRequest.builder()
//                .page(page)
//                .size(size)
//                .sort(sort)
//                .sortDirection(SortDirection.valueOf(sortDirection.toUpperCase()))
//                .build();
//        PaginatedResponse response = registerService.getAllRegistersPaginated(request);
//        return ResponseEntity.ok(response);
//    }

}