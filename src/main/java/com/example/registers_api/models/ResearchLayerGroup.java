package com.example.registers_api.models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResearchLayerGroup {
    private String researchLayerId;
    private String researchLayerName;
    private List<Variable> variables;
}
