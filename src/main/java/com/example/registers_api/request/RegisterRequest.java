package com.example.registers_api.request;

import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.HealthProfessional;
import com.example.registers_api.models.Patient;
import com.example.registers_api.models.Variable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class RegisterRequest {
    @NotNull(message = "Las variables no pueden ser nulas")
    private List<Variable> variables;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private Patient patient;
    private Caregiver caregiver;
    private HealthProfessional healthProfessional;
}
