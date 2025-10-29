package com.example.registers_api.services;

import com.example.registers_api.dtos.FileDownloadDTO;
import com.example.registers_api.services.impl.DocumentService;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.mock.web.MockMultipartFile;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentServiceTest {
    @Mock
    private GridFsTemplate gridFsTemplate;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveConsentimiento_ShouldStoreFileAndReturnId() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "dummy content".getBytes()
        );

        ObjectId objectId = new ObjectId();
        when(gridFsTemplate.store(any(InputStream.class), eq("test.pdf"), eq("application/pdf"), any(DBObject.class)))
                .thenReturn(objectId);

        String result = documentService.saveConsentimiento(file, 123);

        assertNotNull(result);
        assertEquals(objectId.toString(), result);
        verify(gridFsTemplate, times(1))
                .store(any(InputStream.class), eq("test.pdf"), eq("application/pdf"), any(DBObject.class));
    }

    @Test
    void downloadByPatientId_ShouldReturnFile_WhenExists() throws IOException {
        Integer patientId = 123;
        byte[] fileContent = "PDF data".getBytes();

        GridFSFile gridFSFile = mock(GridFSFile.class);
        when(gridFSFile.getFilename()).thenReturn("file.pdf");

        GridFsResource resource = mock(GridFsResource.class);
        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(fileContent));

        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(gridFSFile);
        when(gridFsTemplate.getResource(gridFSFile)).thenReturn(resource);

        FileDownloadDTO result = documentService.downloadByPatientId(patientId);

        assertNotNull(result);
        assertEquals("file.pdf", result.getFilename());
        assertArrayEquals(fileContent, result.getContent());
        verify(gridFsTemplate).findOne(Query.query(Criteria.where("metadata.patientId").is(patientId)));
    }

    @Test
    void downloadByPatientId_ShouldReturnNull_WhenNotFound() throws IOException {
        when(gridFsTemplate.findOne(any(Query.class))).thenReturn(null);

        FileDownloadDTO result = documentService.downloadByPatientId(999);

        assertNull(result);
    }

    // ============================
    // ✅ TEST: downloadAll() con archivos
    // ============================
    @Test
    void downloadAll_ShouldReturnZip_WhenFilesExist() throws IOException {
        GridFSFile file1 = mock(GridFSFile.class);
        GridFSFile file2 = mock(GridFSFile.class);
        when(file1.getFilename()).thenReturn("test1.pdf");
        when(file2.getFilename()).thenReturn("test2.pdf");

        // ✅ Creamos un mock de GridFSFindIterable
        GridFSFindIterable mockIterable = mock(GridFSFindIterable.class);

        // Simulamos el comportamiento del método 'into'
        when(mockIterable.into(anyList())).thenAnswer(invocation -> {
            List<GridFSFile> list = invocation.getArgument(0);
            list.add(file1);
            list.add(file2);
            return list;
        });

        when(gridFsTemplate.find(any(Query.class))).thenReturn(mockIterable);

        // ✅ Simulamos recursos asociados a los archivos
        GridFsResource resource1 = mock(GridFsResource.class);
        GridFsResource resource2 = mock(GridFsResource.class);

        when(gridFsTemplate.getResource(file1)).thenReturn(resource1);
        when(gridFsTemplate.getResource(file2)).thenReturn(resource2);

        when(resource1.getInputStream()).thenReturn(new ByteArrayInputStream("file1-content".getBytes()));
        when(resource2.getInputStream()).thenReturn(new ByteArrayInputStream("file2-content".getBytes()));

        // Ejecutamos el método
        byte[] zipBytes = documentService.downloadAll();

        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        // ✅ Validamos que el ZIP contenga ambos archivos
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry1 = zipInputStream.getNextEntry();
            assertNotNull(entry1);
            assertTrue(entry1.getName().contains("test1.pdf") || entry1.getName().contains("test2.pdf"));

            ZipEntry entry2 = zipInputStream.getNextEntry();
            assertNotNull(entry2);
            assertTrue(entry2.getName().contains("test1.pdf") || entry2.getName().contains("test2.pdf"));
        }

        verify(gridFsTemplate, times(2)).getResource(any(GridFSFile.class));
    }
}
