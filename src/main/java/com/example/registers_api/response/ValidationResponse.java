package com.example.registers_api.response;

import com.example.registers_api.dtos.ResearchLayerInfoDTO;
import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResponse {
    private String action; // patient_already_exist_in_layer | patient_doesnt_exist_in_layer | patient_doesnt_exist
    private String registerId;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private List<ResearchLayerInfoDTO> registerInfo;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
}
