package com.example.registers_api.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VariablesResponse {
    private String id;
    private String researchLayerId;
    private String variableName;
    private String description;
    private String type;
    private Boolean hasOptions;
    private Boolean isEnabled;
    private List<String> options;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
