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
    RegisterCollection findByPatientIdentificationNumber(Integer patientIdentificationNumber);

    List<RegisterCollection> findAllByRegisterInfoResearchLayerId(String researchLayerId, PageRequest pageable); //a

    Optional<RegisterCollection> findById(String id);
    Integer countByPatientIdentificationNumber(Integer patientIdentificationNumber);;
    Integer countByRegisterInfoResearchLayerId(String researchLayerId); //a

//    @Query("{ 'variables.id': ?0 }")
    boolean existsByRegisterInfoVariablesId(String variableId);

//    @Query("{ 'variables.researchLayerId': ?0 }")
    boolean existsByRegisterInfoResearchLayerId(String researchLayerId);

    Optional<RegisterCollection> findFirstByPatientIdentificationNumberOrderByVersionDesc(Integer patientIdentificationNumber);

    // (Opcional) si quieres forzar index scan mínimo:
    @Query(value = "{ 'patientIdentificationNumber': ?0 }", sort = "{ 'version': -1 }")
    Optional<RegisterCollection> findCurrentByPIN(Integer patientIdentificationNumber);
}
