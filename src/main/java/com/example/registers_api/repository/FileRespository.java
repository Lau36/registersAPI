package com.example.registers_api.repository;

import com.example.registers_api.models.FileCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRespository  extends MongoRepository<FileCollection, String> {
}

