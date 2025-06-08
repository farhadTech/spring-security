package com.security.notes.service;

import com.security.notes.dtos.UserDTO;
import com.security.notes.model.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);
}
