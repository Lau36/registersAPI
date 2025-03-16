package com.example.registers_api.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Variable{
    private String id;
    private Object value;
    private String type;
    private String researchLayerId;

    public void setValue(Object value) {
        if ("number".equals(this.type) && !(value instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer cuando el tipo es 'number'.");
        }
        if ("text".equals(this.type) && !(value instanceof String)) {
            throw new IllegalArgumentException("El valor debe ser un String cuando el tipo es 'text'.");
        }
        this.value = value;
    }

    public String toString() {
        return "Variable{id='" + id + "', value=" + value + ", type='" + type + ", researchLayerId=' " + researchLayerId + "}";
    }
}
