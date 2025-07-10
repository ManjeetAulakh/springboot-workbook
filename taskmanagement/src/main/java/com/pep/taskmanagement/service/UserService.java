package com.pep.taskmanagement.service;

import java.util.List;

import com.pep.taskmanagement.models.User;

public interface UserService {
    
    public User createUser(User user);
    public User getUserById(long userId);
    public List<User> getAllUsers();
    public void deleteUser(long userId);
    public User updateRoles(long userId, String roleName);
    
} 