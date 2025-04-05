package com.example.registers_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VariableDTO {

    private String id;
    private String researchLayerId;
    private String variableName;
    private String description;
    private String type;
    private List<String> options;


}
