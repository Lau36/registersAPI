package com.example.registers_api.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class VariableDTO {

    private String id;
    private String idCapaInvestigacion;
    private String nombreVariable;
    private String descripcion;
    private String tipo;
    private List<String> opciones;


}
