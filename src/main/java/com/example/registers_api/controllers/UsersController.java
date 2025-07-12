package com.example.registers_api.controllers;

import com.example.registers_api.dtos.UserDTO;
import com.example.registers_api.response.BasicResponse;
import com.example.registers_api.services.IUserService;
import com.example.registers_api.utils.Constants;
import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.registers_api.utils.Constants.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UsersController {

    private final IUserService userService;

    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    @GetMapping("/GetAll")
    public List<UserRepresentation> findAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping()
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public List<UserRepresentation> getUserByEmail(@RequestParam String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> create(@RequestBody UserDTO user){
        BasicResponse response = new BasicResponse(userService.createUser(user));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.DOCTOR_ROLE + "') or hasRole('" + Constants.RESEARCHER_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> updateUser(@RequestParam String userId, @RequestBody UserDTO userDTO){
        userService.updateUser(userId, userDTO);
        BasicResponse response = new BasicResponse(USER_UPDATED);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> deleteUser(@RequestParam String userId){
        userService.deleteUser(userId);
        BasicResponse response = new BasicResponse(USER_DELETED);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/disableUser")
    @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> disableUser(@RequestParam String userId){
        userService.disableOrEnableUser(userId, false);
        BasicResponse response = new BasicResponse(USER_DELETED);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/enabledUser")
   @PreAuthorize("hasRole('" + Constants.ADMIN_ROLE + "') or hasRole('" + Constants.SUPER_ADMIN_ROLE + "')")
    public ResponseEntity<BasicResponse> enabledUser(@RequestParam String userId){
        userService.disableOrEnableUser(userId, true);
        BasicResponse response = new BasicResponse(USER_DELETED);
        return ResponseEntity.ok(response);
    }

}
