package com.example.registers_api.repository;

import com.example.registers_api.models.RegistersHistoryCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface RegisterHistoryRepository extends MongoRepository<RegistersHistoryCollection, String> {
    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperationAndIsResearchLayerGroup_ResearchLayerId(
            Integer patientIdentificationNumber,
            String operation,
            String researchLayerId,
            Pageable pageable
    );

    @Query(
            value = "{ 'patientIdentificationNumber': ?0, 'operation': { $in: ?1 } }",
            fields = "{ 'id': 1, 'registerId': 1, 'changedBy': 1, 'changedAt': 1, 'operation': 1, 'patientIdentificationNumber': 1, 'isCaregiverInfo': 1 }"
    )
    Page<RegistersHistoryCollection> findCaregiverHistoryByPatientAndOps(Integer patientId, Collection<String> ops, Pageable pageable);

    @Query(
            value = "{ 'patientIdentificationNumber': ?0, " +
                    "  'operation': { $in: ?1 }, " +
                    "  'isResearchLayerGroup.researchLayerId': ?2 }",
            fields = "{ 'id': 1, 'registerId': 1, 'changedBy': 1, 'changedAt': 1, " +
                    "  'operation': 1, 'patientIdentificationNumber': 1, " +
                    "  'isResearchLayerGroup': 1 }"
    )
    Page<RegistersHistoryCollection> findResearchLayerHistoryByPatientAndOps(
            Integer patientIdentificationNumber,
            Collection<String> ops,
            String researchLayerId,
            Pageable pageable
    );

    @Query(
            value = "{ 'isResearchLayerGroup.researchLayerId': ?0, 'operation': { $in: ?1 } }",
            fields = "{ 'id': 1, 'registerId': 1, 'changedBy': 1, 'changedAt': 1, " +
                    "  'operation': 1, 'patientIdentificationNumber': 1, " +
                    "  'isResearchLayerGroup': 1 }"
    )
    Page<RegistersHistoryCollection> findResearchLayerHistoryByResearchLayerIdAndOps(
            String researchLayerId,
            Collection<String> ops,
            Pageable pageable
    );

    @Query(
            value = "{ 'patientIdentificationNumber': ?0, 'operation': { $in: ?1 } }",
            fields = "{ 'id': 1, 'registerId': 1, 'changedBy': 1, 'changedAt': 1, 'operation': 1, 'patientIdentificationNumber': 1, 'isPatientBasicInfo': 1 }"
    )
    Page<RegistersHistoryCollection> findPatientHistoryByPatientAndOps(Integer patientId, Collection<String> ops, Pageable pageable);

    Page<RegistersHistoryCollection>
    findAllByPatientIdentificationNumberAndOperationInAndIsResearchLayerGroup_ResearchLayerId(
            Integer patientIdentificationNumber,
            Collection<String> operations,
            String researchLayerId,
            Pageable pageable
    );

    Page<RegistersHistoryCollection>
    findAllByOperationAndIsResearchLayerGroup_ResearchLayerId(
            String researchLayerId,
            Collection<String> operations,
            Pageable pageable
    );

    RegistersHistoryCollection findByPatientIdentificationNumberAndOperation(Integer patientIdentificationNumber,
                                                                             String operation);
    RegistersHistoryCollection findByOperationAndIsResearchLayerGroup_ResearchLayerId(String researchLayerId,
                                                                             String operation);

    Page<RegistersHistoryCollection> findAllByPatientIdentificationNumberAndOperationIn(
            Integer patientIdentificationNumber,
            Collection<String> operations,
            Pageable pageable
    );

    long deleteByRegisterId(String registerId);
}
