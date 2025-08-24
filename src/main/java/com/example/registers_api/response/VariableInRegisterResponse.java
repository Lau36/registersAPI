package com.example.registers_api.response;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class VariableInRegisterResponse {
    private String variableId;
    private String variableName;
    private String variableType;
    private String valueAsString;
    private Double valueAsNumber;

}
