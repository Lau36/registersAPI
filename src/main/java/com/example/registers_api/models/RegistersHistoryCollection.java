package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "registersHistory")
public class RegistersHistoryCollection {
    @Id
    private String id;
    private LocalDateTime registerId;
    private Integer patientIdentificationNumber;
    private LocalDateTime updateRegisterDate;
    private String updatedBy;
    private List<ResearchLayerGroup> registerInfo;
    private String patientIdentificationType;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
    private HealthProfessional healthProfessional;
}
