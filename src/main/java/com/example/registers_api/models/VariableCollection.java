package com.example.registers_api.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "variablesView")
public class VariableCollection {

    @Id
    private String id;

    private String researchLayerId;

    private String variableName;

    private String description;

    private String type;

    private Boolean hasOptions;

    private Boolean isEnabled;

    private List<String> options;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
