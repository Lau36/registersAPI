package com.example.registers_api.repository;

import com.example.registers_api.models.RegistersHistoryCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisterHistoryRepository extends MongoRepository<RegistersHistoryCollection, String> {
}
