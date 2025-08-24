package com.example.registers_api.models;

import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Variable{
    private String id;
    private String name;
    private String type;
    private String valueAsString;
    private Double valueAsNumber;

}
