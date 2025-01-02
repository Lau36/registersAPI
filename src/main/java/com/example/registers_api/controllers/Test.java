package com.example.registers_api.controllers;

import com.example.registers_api.services.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Test")
@RequiredArgsConstructor
public class Test {

    private final TestService testService;

    @PostMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(testService.test());
    }
}
