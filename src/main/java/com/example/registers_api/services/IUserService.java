package com.example.registers_api.services;

import com.example.registers_api.dtos.UserDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IUserService {

    String createUser (UserDTO user);
    List<UserRepresentation> getAllUsers();
    List<UserRepresentation> getUserByEmail(String username);
    void deleteUser(String userId);
    void disableOrEnableUser(String userId, boolean isEnabled);
    void updateUser(String userId, UserDTO userDTO);
}
