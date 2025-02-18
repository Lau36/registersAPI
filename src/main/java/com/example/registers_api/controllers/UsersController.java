package com.example.registers_api.controllers;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.IUserService;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.registers_api.utils.Constants.*;

@RestController
@RequestMapping("/Users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
//@PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
public class UsersController {

    private final IUserService userService;


    @GetMapping("/GetAll")
    public List<UserRepresentation> findAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get/{userId}")
    public UserResource getUserById(@PathVariable String userId){
        return userService.getUserById(userId);
    }

    @PostMapping("/create")
    public ResponseEntity<BasicResponse> create(@RequestBody UserDTO user){
        BasicResponse response = new BasicResponse(userService.createUser(user));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<BasicResponse> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO){
        userService.updateUser(userId, userDTO);
        BasicResponse response = new BasicResponse(USER_UPDATED);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<BasicResponse> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        BasicResponse response = new BasicResponse(USER_DELETED);
        return ResponseEntity.ok(response);
    }

}
