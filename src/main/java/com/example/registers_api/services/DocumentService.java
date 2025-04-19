package com.example.registers_api.services;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {
    ObjectId storeFile(MultipartFile file, String uploadedBy) throws IOException;
    GridFsResource getFileById(String id);
    GridFsResource getFileByFilename(String filename);
}
