package com.pep.springjpa.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pep.springjpa.exception.UserNotFoundException;
import com.pep.springjpa.models.User;
import com.pep.springjpa.repo.UserRepo;

import jakarta.validation.Valid;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User getById(int id) {
        return userRepo.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));
    }

    public List<User> getAllUser(){
        return userRepo.findAll();
    }

    public User updateUser(int id, @Valid User user) {
        User existingUser = userRepo.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        return userRepo.save(existingUser);
    }

    public ResponseEntity<Void> deleteUser(int id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
