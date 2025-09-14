package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "analytics_register")
public class AnalyticsRegisterCollection {

    @Id
    private String id;

    private String registerId;
    private LocalDateTime readingTimestamp;

    // Research Layer & Variable
    private String researchLayerName;
    private String variableName;
    private String variableType;
    private Object variableValue;

    // Patient info
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private String patientName;
    private String patientSex;
    private Integer patientAgeAtReading;
    private String patientEmail;
    private String patientPhoneNumber;
    private String patientEconomicStatus;
    private String patientEducationLevel;
    private String patientMaritalStatus;
    private String patientHometown;
    private String patientCurrentCity;
    private String patientFirstCrisisDate;
    private String patientCrisisStatus;

    // Caregiver info
    private String caregiverName;
    private Integer caregiverIdentificationNumber;
    private String caregiverOccupation;

}
