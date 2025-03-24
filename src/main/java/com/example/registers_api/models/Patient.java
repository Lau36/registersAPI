package com.example.registers_api.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Patient {
    @NotNull(message = "El name no puede ser nulo")
    private String name;

    @NotNull(message = "El sexo no puede ser nulo")
    private String sex;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate birthDate;

    @NotNull(message = "La edad no puede ser nula")
    private Integer age;

    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotNull(message = "El número de teléfono no puede ser nulo")
    private String phoneNumber;

    private LocalDate deathDate;

    @NotNull(message = "El estado económico no puede ser nulo")
    private String economicStatus;

    @NotNull(message = "El nivel educativo no puede ser nulo")
    private String educationLevel;

    @NotNull(message = "El estado civil no puede ser nulo")
    private String maritalStatus;

    @NotNull(message = "El pueblo natal no puede ser nulo")
    private String hometown;

    @NotNull(message = "La ciudad actual no puede ser nula")
    private String currentCity;

    @NotNull(message = "La fecha de la primera crisis no puede ser nula")
    private String firstCrisisDate;

    @NotNull(message = "El estado de crisis no puede ser nulo")
    private String crisisStatus;

}
