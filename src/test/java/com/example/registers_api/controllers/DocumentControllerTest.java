package com.example.registers_api.controllers;

import com.example.registers_api.dtos.FileDownloadDTO;
import com.example.registers_api.services.IDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentControllerTest {

    @Mock
    private IDocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- TEST: uploadConsentimiento - éxito ---
    @Test
    void uploadConsentimiento_shouldReturnSuccessResponse() throws Exception {
        // Arrange
        int patientId = 123;
        MultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", "contenido".getBytes());
        when(documentService.saveConsentimiento(mockFile, patientId)).thenReturn("file123");

        // Act
        ResponseEntity<String> response = documentController.uploadConsentimiento(patientId, mockFile);

        // Assert
        verify(documentService).saveConsentimiento(mockFile, patientId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Archivo guardado con ID: file123", response.getBody());
    }

    // --- TEST: uploadConsentimiento - error ---
    @Test
    void uploadConsentimiento_shouldReturnErrorResponseWhenExceptionOccurs() throws Exception {
        // Arrange
        int patientId = 123;
        MultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", "contenido".getBytes());
        when(documentService.saveConsentimiento(mockFile, patientId)).thenThrow(new RuntimeException("Fallo"));

        // Act
        ResponseEntity<String> response = documentController.uploadConsentimiento(patientId, mockFile);

        // Assert
        verify(documentService).saveConsentimiento(mockFile, patientId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error al guardar el archivo"));
    }

    // --- TEST: downloadConsentimiento - éxito ---
    @Test
    void downloadConsentimiento_shouldReturnFileData() throws IOException {
        // Arrange
        int patientId = 123;
        FileDownloadDTO fileData = new FileDownloadDTO("consentimiento.pdf", "PDF CONTENT".getBytes());
        when(documentService.downloadByPatientId(patientId)).thenReturn(fileData);

        // Act
        ResponseEntity<byte[]> response = documentController.downloadConsentimiento(patientId);

        // Assert
        verify(documentService).downloadByPatientId(patientId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals("attachment; filename=\"consentimiento.pdf\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals("PDF CONTENT".getBytes(), response.getBody());
    }

    // --- TEST: downloadConsentimiento - archivo no encontrado ---
    @Test
    void downloadConsentimiento_shouldReturnNotFoundWhenNoFile() throws IOException {
        // Arrange
        int patientId = 123;
        when(documentService.downloadByPatientId(patientId)).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = documentController.downloadConsentimiento(patientId);

        // Assert
        verify(documentService).downloadByPatientId(patientId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // --- TEST: downloadAllDocuments - éxito ---
    @Test
    void downloadAllDocuments_shouldReturnZipFile() throws IOException {
        // Arrange
        byte[] zipData = "ZIPDATA".getBytes();
        when(documentService.downloadAll()).thenReturn(zipData);

        // Act
        ResponseEntity<byte[]> response = documentController.downloadAllDocuments();

        // Assert
        verify(documentService).downloadAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());
        assertEquals("attachment; filename=\"all_documents.zip\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(zipData, response.getBody());
    }

    // --- TEST: downloadAllDocuments - sin documentos ---
    @Test
    void downloadAllDocuments_shouldReturnNotFoundWhenEmpty() throws IOException {
        // Arrange
        when(documentService.downloadAll()).thenReturn(new byte[0]);

        // Act
        ResponseEntity<byte[]> response = documentController.downloadAllDocuments();

        // Assert
        verify(documentService).downloadAll();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // --- TEST: downloadAllDocuments - null response ---
    @Test
    void downloadAllDocuments_shouldReturnNotFoundWhenNull() throws IOException {
        // Arrange
        when(documentService.downloadAll()).thenReturn(null);

        // Act
        ResponseEntity<byte[]> response = documentController.downloadAllDocuments();

        // Assert
        verify(documentService).downloadAll();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
