package com.example.registers_api.repository;

import com.example.registers_api.models.RegistersHistoryCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegisterHistoryRepository extends MongoRepository<RegistersHistoryCollection, String> {
    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperationAndIsResearchLayerGroup_ResearchLayerId(
            Integer patientIdentificationNumber,
            String operation,
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
}
