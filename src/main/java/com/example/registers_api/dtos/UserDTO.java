package com.example.registers_api.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String identificationType;
    private String identificationNumber;
    private LocalDate birthDate;
    private String researchLayer;
    private String role;
}
