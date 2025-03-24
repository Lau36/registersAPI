package com.example.registers_api.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class LayerBossDTO {
    private final int id;
    private final String name;
    private final String identificationNumber;
}
