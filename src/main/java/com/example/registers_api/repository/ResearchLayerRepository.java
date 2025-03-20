package com.example.registers_api.repository;

import com.example.registers_api.models.ResearchLayerCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResearchLayerRepository extends MongoRepository<ResearchLayerCollection, String> {
    boolean existsByNombreCapa(String nombreCapa);
    boolean existsById(String id);
    Optional<ResearchLayerCollection> findById(String id);
    Optional<ResearchLayerCollection> findByNombreCapa(String name);
}
