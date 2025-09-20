package com.example.registers_api.response;

import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.Patient;
import com.example.registers_api.models.ResearchLayerGroup;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistersHistoryItemResponse {
    private String id;
    private String registerId;
    private String changedBy;
    private String changedAt;
    private String operation;
    private Integer patientIdentificationNumber;
    private ResearchLayerGroup isResearchLayerGroup;
    private Patient isPatientBasicInfo;
    private Caregiver isCaregiverInfo;
}
