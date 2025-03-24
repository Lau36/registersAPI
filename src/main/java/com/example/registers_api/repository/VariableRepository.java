package com.example.registers_api.repository;

import com.example.registers_api.models.VariableCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VariableRepository extends MongoRepository<VariableCollection, String> {
    boolean existsByVariableNameAndIsEnabled(String name, boolean isEnabled);
    boolean existsById(String id);
    List<VariableCollection> findAllByResearchLayerIdAndIsEnabled(String idCapaInvestigacion, boolean isEnabled);
    List<VariableCollection> findAllByIsEnabled( boolean isEnabled);


    Optional<VariableCollection> findByIdAndIsEnabled(String IdVariable, boolean isEnabled);
    Optional<VariableCollection> findByVariableNameAndIsEnabled(String name, boolean isEnabled);
    Optional<VariableCollection> findByVariableName(String name);
}

