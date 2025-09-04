package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "termsAndConditions")
public class TermsAndConditionsCollection {
    @Id
    private String id;
    private String termsConditionsId;
    private String termsConditions;
}

