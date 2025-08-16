package com.example.registers_api.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
public class VariableRequest {
    private String id;
    private String variableName;
    private Object value;
    private String type;
    private String researchLayerId;
    private String researchLayerName;

    public void setValue(Object value) {
        if ("number".equals(this.type) && !(value instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer cuando el type es 'number'.");
        }
        if ("text".equals(this.type) && !(value instanceof String)) {
            throw new IllegalArgumentException("El valor debe ser un String cuando el type es 'text'.");
        }
        this.value = value;
    }

    public String toString() {
        return "Variable{id='" + id + "', value=" + value + ", type='" + type + ", researchLayerId=' " + researchLayerId + "}";
    }
}