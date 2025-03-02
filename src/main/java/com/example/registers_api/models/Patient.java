package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Patient {
    private String name;
    private String identificationType;
    private Integer identificationNumber;
    private String sex;
    private int age;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private LocalDate deathDate;
    private String economicSatus;
    private String matirialSatus;
    private String maritialStatus;
    private String hometown;
    private String currentCity;
    private String firstCrisisDate;
    private String crisisStatus;
    private String registerDate;

}
