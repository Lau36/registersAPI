package com.example.registers_api.controllers;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.services.UsersService;
import com.example.registers_api.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/User")
@PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
@AllArgsConstructor
public class UsersController {

        private final UsersService usersService;

        @GetMapping("/search")
        public ResponseEntity<?> findAllUsers(){
            return ResponseEntity.ok(usersService.findAllUsers());
        }


        @PostMapping("/create")
        public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
            String response = usersService.createUser(userDTO);
            return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
        }



}
