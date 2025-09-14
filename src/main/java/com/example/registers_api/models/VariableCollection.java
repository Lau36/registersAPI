package com.example.registers_api.models;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Data
@Document(collection = "variablesView")
public class VariableCollection {

    @Id
    private String id;

    @NonNull
    private String researchLayerId;

    @NonNull
    private String variableName;

    @NonNull
    private String description;

    private String type;

    private Boolean hasOptions;

    private Boolean isEnabled;

    private List<String> options;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
