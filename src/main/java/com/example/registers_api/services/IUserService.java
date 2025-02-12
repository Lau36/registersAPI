package com.example.registers_api.services;

import com.example.registers_api.dtos.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IUserService {

    String createUser (UserDTO user);
    List<UserRepresentation> getAllUsers();
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);
}
