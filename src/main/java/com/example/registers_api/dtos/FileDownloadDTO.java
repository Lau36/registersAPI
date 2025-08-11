package com.example.registers_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileDownloadDTO {
    private String filename;
    private byte[] content;
}
