package com.example.registers_api.response;

import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterResponse2 {
    private String registerId;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private List<ResearchLayerGroupResponse> registerInfo;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
}
