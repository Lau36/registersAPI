package com.example.registers_api.repository;

import com.example.registers_api.models.RegisterCollection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegisterRepository extends MongoRepository<RegisterCollection, String> {
    List<RegisterCollection> findAllBy(PageRequest pageRequest);
    List<RegisterCollection> findAllByPatientIdentificationNumber(Integer patientIdentificationNumber, PageRequest pageable);
    RegisterCollection findByPatientIdentificationNumber(Integer patientIdentificationNumber);

    //@Query("{ 'healthProfessional.identificationNumber': ?0 }")
    List<RegisterCollection> findAllByHealthProfessionalIdentificationNumber(Integer identificationNumber, PageRequest pageable);

    List<RegisterCollection> findAllByRegisterInfoResearchLayerId(String researchLayerId, PageRequest pageable); //a

    Optional<RegisterCollection> findById(String id);
    Integer countByPatientIdentificationNumber(Integer patientIdentificationNumber);
    Integer countByHealthProfessionalIdentificationNumber(Integer identificationNumber);
    Integer countByRegisterInfoResearchLayerId(String researchLayerId); //a


//    @Query("{ 'variables.id': ?0 }")
    boolean existsByRegisterInfoVariablesId(String variableId);

//    @Query("{ 'variables.researchLayerId': ?0 }")
    boolean existsByRegisterInfoResearchLayerId(String researchLayerId);
}
