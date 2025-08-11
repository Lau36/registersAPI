package com.example.registers_api.services.impl;

import com.example.registers_api.dtos.FileDownloadDTO;
import com.example.registers_api.services.IDocumentService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@AllArgsConstructor
public class DocumentService implements IDocumentService {

    private final GridFsTemplate gridFsTemplate;

    @Override
    public String saveConsentimiento(MultipartFile file, int patientId) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("patientId", patientId);
        metaData.put("type", file.getContentType());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(),
                file.getOriginalFilename(),
                file.getContentType(),
                metaData
        );

        return id.toString();
    }

    @Override
    public FileDownloadDTO downloadByPatientId(Integer patientId) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(
                Query.query(Criteria.where("metadata.patientId").is(patientId))
        );

        if (file == null) {
            return null;
        }

        GridFsResource resource = gridFsTemplate.getResource(file);
        byte[] content = IOUtils.toByteArray(resource.getInputStream());

        return new FileDownloadDTO(file.getFilename(), content);
    }

//    @Override
//    public GridFsResource getConsentimiento(String patientId) throws IOException {
//        GridFSFile file = gridFsTemplate.findOne(
//                new Query(Criteria.where("metadata.patientId").is(patientId))
//                        .with(Sort.by(Sort.Direction.DESC, "uploadDate"))
//        );
//
//        if (file == null) {
//            throw new FileNotFoundException("Archivo no encontrado con id: " + patientId);
//        }
//
//        return gridFsTemplate.getResource(file);
//    }
}
