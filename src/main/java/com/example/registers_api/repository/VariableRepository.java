package com.example.registers_api.repository;

import com.example.registers_api.models.VariableCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VariableRepository extends MongoRepository<VariableCollection, String> {
    boolean existsByNombreVariable(String name);
    List<VariableCollection> findAllByIdCapaInvestigacion(String idCapaInvestigacion);

    Optional<VariableCollection> findById(String IdVariable);
}

