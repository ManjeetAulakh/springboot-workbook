package com.pep.springjpa.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pep.springjpa.exception.UserNotFoundException;
import com.pep.springjpa.models.User;
import com.pep.springjpa.repo.UserRepo;

import jakarta.validation.Valid;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.getRoles().add("ROLE_USER");
        }

        return userRepo.save(user);
    }

    public List<User> createUsers(List<User> users) {

        users.forEach(user -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.getRoles().add("ROLE_USER");
            }
        });

        // using StreamSupport
        Iterable<User> savedUsers = userRepo.saveAll(users);
        return StreamSupport.stream(savedUsers.spliterator(), false)
                .collect(Collectors.toList());

        /*
         * by using loop
         * Iterable<User> savedUsers = userRepo.saveAll(users);
         * List<User> result = new ArrayList<>();
         * savedUsers.forEach(result::add);
         * return result;
         */

    }

    public User getById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));
    }

    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    public Page<User> getUserPage(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepo.findAll(pageable);

        // Bonus: Return Only List (Without Page Metadata)

        /*
         * public List<User> getUsersListByPage(int page, int size) {
         * Pageable pageable = PageRequest.of(page, size);
         * return userRepository.findAll(pageable).getContent();
         * }
         */

    }

    public User updateUser(int id, @Valid User user) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with Id: " + id));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

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
