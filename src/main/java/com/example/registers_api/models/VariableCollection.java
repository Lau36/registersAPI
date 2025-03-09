package com.example.registers_api.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Data
@Document(collection = "variablesView")
public class VariableCollection {

    @Id
    private String id;

    @NonNull
    private String idCapaInvestigacion;

    @NonNull
    private String nombreVariable;

    @NonNull
    private String descripcion;

    private String tipo;

    private Boolean tieneOpciones;

    private List<String> opciones;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;


}
