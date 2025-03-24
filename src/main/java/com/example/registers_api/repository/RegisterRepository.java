package com.example.registers_api.repository;

import com.example.registers_api.models.RegisterCollection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegisterRepository extends MongoRepository<RegisterCollection, String> {
    List<RegisterCollection> findAllBy(PageRequest pageRequest);
    List<RegisterCollection> findAllByPatientIdentificationNumber(Integer patientIdentificationNumber, PageRequest pageable);

    //@Query("{ 'healthProfessional.identificationNumber': ?0 }")
    List<RegisterCollection> findAllByHealthProfessionalIdentificationNumber(Integer identificationNumber, PageRequest pageable);

    Optional<RegisterCollection> findById(String id);
    Integer countByPatientIdentificationNumber(Integer patientIdentificationNumber);
    Integer countByHealthProfessionalIdentificationNumber(Integer identificationNumber);

    @Query("{ 'variables.id': ?0 }")
    boolean existsByVariableId(String variableId);

    @Query("{ 'variables.researchLayerId': ?0 }")
    boolean existsByResearchLayerId(String researchLayerId);
}
