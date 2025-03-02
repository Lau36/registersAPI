package com.example.registers_api.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "registers")
public class RegisterCollection {

    @Id
    private String id;
    private LocalDateTime registerDate;
    private List<Variable> variables;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
    private HealtProfessional healtProfessional;


}
