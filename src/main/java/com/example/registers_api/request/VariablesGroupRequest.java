package com.example.registers_api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VariablesGroupRequest {
    private String id;
    private String name;
    private Object value;
    private String type;
}
