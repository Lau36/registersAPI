package com.example.registers_api.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "registers")
@TimeSeries(
        timeField = "registerDate",
        metaField = "patientIdentificationNumber",
        granularity = Granularity.HOURS
)
public class RegisterCollection {

    @Id
    private String id;
    private LocalDateTime registerDate;
    private LocalDateTime updateRegisterDate;
    private List<Variable> variables;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
    private HealthProfessional healthProfessional;


}
