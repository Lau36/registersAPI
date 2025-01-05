package com.example.registers_api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LayerBoss {

    private int id;
    private String nombre;
    private String numero_identificacion;
}
