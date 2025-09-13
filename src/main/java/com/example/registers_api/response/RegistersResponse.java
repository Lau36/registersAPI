package com.example.registers_api.response;

import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.HealthProfessional;
import com.example.registers_api.models.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegistersResponse {
    private String registerId;
    private LocalDateTime registerDate;
    private LocalDateTime updateRegisterDate;
    private String updatedBy;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private List<ResearchLayerGroupResponse> registerInfo;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
    private HealthProfessional healthProfessional;
}
