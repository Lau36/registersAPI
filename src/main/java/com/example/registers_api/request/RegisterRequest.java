package com.example.registers_api.request;

import com.example.registers_api.models.Caregiver;
import com.example.registers_api.models.HealtProfessional;
import com.example.registers_api.models.Patient;
import com.example.registers_api.models.Variable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {
    private List<Variable> variables;
    private Patient patient;
    private Caregiver caregiver;
    private HealtProfessional healtProfessional;
}
