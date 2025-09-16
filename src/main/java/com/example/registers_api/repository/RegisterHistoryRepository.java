package com.example.registers_api.repository;

import com.example.registers_api.models.RegistersHistoryCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface RegisterHistoryRepository extends MongoRepository<RegistersHistoryCollection, String> {
    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperationAndIsResearchLayerGroup_ResearchLayerId(
            Integer patientIdentificationNumber,
            String operation,
            String researchLayerId,
            Pageable pageable
    );

    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperationInAndIsResearchLayerGroup_ResearchLayerId(
            Integer patientIdentificationNumber,
            Collection<String> operations,
            String researchLayerId,
            Pageable pageable
    );

    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperation(
            Integer patientIdentificationNumber,
            String operation,
            Pageable pageable
    );

    RegistersHistoryCollection findByPatientIdentificationNumberAndOperation(Integer patientIdentificationNumber,
                                                                             String operation);

    Page<RegistersHistoryCollection> findAllByPatientIdentificationNumberAndOperationIn(
            Integer patientIdentificationNumber,
            Collection<String> operations,
            Pageable pageable
    );
}
