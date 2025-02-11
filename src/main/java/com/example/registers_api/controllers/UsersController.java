package com.example.registers_api.controllers;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.services.IUserService;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
@AllArgsConstructor
//@PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
public class UsersController {

    private final IUserService userService;


    @GetMapping("/GetAll")
    public List<UserRepresentation> findAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/create/Doctor")
    public String createDoctor(@RequestBody UserDTO user){
        return userService.createDoctor(user);
    }

    @PostMapping("/create/Admin")
    public String createAdmin(@RequestBody UserDTO user){
        return userService.createAdmin(user);
    }

    @PostMapping("/create/Reseacher")
    public String createReseacher(@RequestBody UserDTO user){
        return userService.createReseacher(user);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO){
        userService.updateUser(userId, userDTO);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
