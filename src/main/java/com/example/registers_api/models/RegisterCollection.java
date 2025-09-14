package com.example.registers_api.models;

import com.mongodb.lang.NonNullApi;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;
import org.springframework.data.mongodb.core.timeseries.Granularity;

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
    @Version
    private Long version;
    private Integer patientIdentificationNumber;
    private String patientIdentificationType;
    private List<ResearchLayerGroup> registerInfo;
    private Patient patientBasicInfo;
    private Caregiver caregiver;
}
