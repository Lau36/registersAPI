package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Caregiver {
    private String name;
    private String identificationType;
    private Integer identificationNumber;
    private Integer age;
    private String educationLevel;
    private String occupation;
}
