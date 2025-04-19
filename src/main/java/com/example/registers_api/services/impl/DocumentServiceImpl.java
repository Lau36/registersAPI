package com.example.registers_api.services.impl;

import com.example.registers_api.services.DocumentService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private GridFsTemplate gridFsTemplate;

    private GridFsOperations gridFsOperations;

    public ObjectId storeFile(MultipartFile file, String uploadedBy) throws IOException {
        return gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                Map.of("uploadedBy", uploadedBy)
        );
    }

    public GridFsResource getFileById(String id) {
        GridFSFile file = gridFsTemplate.findOne(query(where("_id").is(new ObjectId(id))));
        return gridFsOperations.getResource(file);
    }

    public GridFsResource getFileByFilename(String filename) {
        GridFSFile file = gridFsTemplate.findOne(query(where("filename").is(filename)));
        return gridFsOperations.getResource(file);
    }
}
