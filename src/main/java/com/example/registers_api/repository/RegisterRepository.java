package com.example.registers_api.repository;

import com.example.registers_api.models.RegisterCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisterRepository extends MongoRepository<RegisterCollection, String> {
}
