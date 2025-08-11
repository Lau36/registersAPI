package com.example.registers_api.services;

import com.example.registers_api.dtos.FileDownloadDTO;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IDocumentService {
    String saveConsentimiento(MultipartFile file, int patientId) throws IOException;
    FileDownloadDTO downloadByPatientId(Integer patientId) throws IOException;
}
