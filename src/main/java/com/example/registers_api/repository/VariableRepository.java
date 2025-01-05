package com.example.registers_api.repository;

import com.example.registers_api.models.VariableCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariableRepository extends MongoRepository<VariableCollection, String> {
    boolean existsByNombreVariable(String name);
}

