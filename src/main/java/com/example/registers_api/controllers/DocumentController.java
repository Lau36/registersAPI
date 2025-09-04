package com.example.registers_api.controllers;

import com.example.registers_api.dtos.FileDownloadDTO;
import com.example.registers_api.models.FileCollection;
import com.example.registers_api.request.PaginationRequest;
import com.example.registers_api.request.RegisterRequest;
import com.example.registers_api.request.SortDirection;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.response.PaginatedResponse;
import com.example.registers_api.services.IDocumentService;
import com.example.registers_api.services.IRegisterService;
import com.example.registers_api.utils.Constants;
import lombok.AllArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/documents")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {

    private final IDocumentService documentService;


    @PostMapping("/upload")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<String> uploadConsentimiento(
            @RequestParam("patientId") int patientId,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileId =documentService.saveConsentimiento(file, patientId);
            return ResponseEntity.ok("Archivo guardado con ID: " + fileId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/download/{patientId}")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<byte[]> downloadConsentimiento(@PathVariable Integer patientId) throws IOException {
        FileDownloadDTO fileData = documentService.downloadByPatientId(patientId);

        if (fileData == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFilename() + "\"")
                .body(fileData.getContent());
    }

    @GetMapping("/download/all")
    @PreAuthorize("hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<byte[]> downloadAllDocuments() throws IOException {
        byte[] zipBytes = documentService.downloadAll();

        if (zipBytes == null || zipBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"all_documents.zip\"")
                .body(zipBytes);
    }

}