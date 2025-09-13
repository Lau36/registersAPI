package com.example.registers_api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfoChanged {
    private Boolean patientBasicInfo;
    private Boolean registerInfo;
    private Boolean caregiver;
}
