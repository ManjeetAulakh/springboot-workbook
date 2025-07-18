package com.pep.taskmanagement.service;

import java.util.List;
import java.util.Set;

import com.pep.taskmanagement.models.User;

public interface UserService {
    
    public User createUser(User user);
    public User getUserById(Long userId);
    public List<User> getAllUsers();
    public void deleteUser(Long userId);
    public User updateRoles(Long userId, Set<String> roleNames);
    
} 