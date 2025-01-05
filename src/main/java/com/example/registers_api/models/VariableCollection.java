package com.example.registers_api.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@RequiredArgsConstructor
@Getter
@Setter
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

    @NonNull
    private String tipo;

}
