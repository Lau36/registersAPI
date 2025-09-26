package com.example.registers_api.repository;

import com.example.registers_api.models.AnalyticsRegisterCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnalitycsRegister extends MongoRepository<AnalyticsRegisterCollection, String> {
    long deleteByRegisterId(String registerId);
}
