package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "register_history")
public class RegistersHistoryCollection {
    @Id
    private String id;
    private String registerId;
    private String changedBy;
    private String changedAt;
    private String operation;
    private ResearchLayerGroup isResearchLayerGroup;
    private Patient isPatientBasicInfo;
    private Caregiver isCaregiverInfo;
}
