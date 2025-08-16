package com.example.registers_api.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class VariableInRegisterResponse {
    private String variableId;
    private String variableName;
    private Object variableValue;
    private String variableType;

    public void setValue(Object value) {
        if ("number".equals(this.variableType) && !(variableValue instanceof Integer)) {
            throw new IllegalArgumentException("El valor debe ser un Integer cuando el type es 'number'.");
        }
        if ("text".equals(this.variableType) && !(variableValue instanceof String)) {
            throw new IllegalArgumentException("El valor debe ser un String cuando el type es 'text'.");
        }
        this.variableValue= value;
    }

    public String toString() {
        return "Variable{id='" + variableId + "', value=" + variableValue+ ", type='" + variableType +  "}";
    }
}
