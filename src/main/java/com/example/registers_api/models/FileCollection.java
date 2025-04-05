package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "consentimientos")
public class FileCollection {
    @Id
    private String id;

    private Integer identifyPatient;
    private String tipoMime;
    private Binary contenido;
    private Date fecha = new Date();
}
