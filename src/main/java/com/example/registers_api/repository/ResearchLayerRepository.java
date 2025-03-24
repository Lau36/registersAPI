package com.example.registers_api.repository;

import com.example.registers_api.models.ResearchLayerCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResearchLayerRepository extends MongoRepository<ResearchLayerCollection, String> {
    boolean existsByLayerNameAndIsEnabled(String layerName, Boolean isEnabled);
    boolean existsById(String id);
    boolean existsByLayerNameAndIsEnabled(String name, boolean isEnabled);

    List<ResearchLayerCollection> findAllByIsEnabled( boolean isEnabled);
    Optional<ResearchLayerCollection> findByIdAndIsEnabled(String IdCapa, boolean isEnabled);
    Optional<ResearchLayerCollection> findByLayerNameAndIsEnabled(String name, boolean isEnabled);

}
