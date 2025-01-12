package com.example.registers_api.repository;

import com.example.registers_api.models.ResearchLayerCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResearchLayerRepository extends MongoRepository<ResearchLayerCollection, String> {
    boolean existsByNombreCapa(String nombreCapa);
}
