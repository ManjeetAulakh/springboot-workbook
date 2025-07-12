package com.pep.taskmanagement.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pep.taskmanagement.exception.RoleException;
import com.pep.taskmanagement.exception.UserException;
import com.pep.taskmanagement.models.Role;
import com.pep.taskmanagement.models.User;
import com.pep.taskmanagement.models.Enums.RoleType;
import com.pep.taskmanagement.repository.RoleRepo;
import com.pep.taskmanagement.repository.UserRepo;
import com.pep.taskmanagement.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private RoleRepo roleRepo;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,
            RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public User createUser(User user) {

        // check username or email already exists or not
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new UserException("User already exists with username: " + user.getUsername());
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new UserException("User already exists with email: " + user.getEmail());
        }

        // set encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set deafult roles
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepo.findByName(RoleType.ROLE_TEAM_MEMBER.name())
                    .orElseThrow(() -> new RoleException("Role is not exists : ROLE_TEAM_MEMBER"));
            roles.add(defaultRole);

        } else {
            for (Role role : user.getRoles()) {
                Role dbRole = roleRepo.findByName(role.getName())
                        .orElseThrow(() -> new RoleException("Role is not exists : " + role.getName()));
                roles.add(dbRole);
            }
        }
        user.setRoles(roles);

        return userRepo.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserException("User not found with this User ID: " + userId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserException("User not found with this User ID: " + userId));

        userRepo.delete(user);
    }

    @Override
    public User updateRoles(Long userId, Set<String> roleNames) {

        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new UserException("User not found with this User ID: " + userId));

        Set<Role> roles = new HashSet<>();

        for (String roleName : roleNames) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RoleException("Role not found: " + roleName));
            roles.add(role);
        }

        existingUser.setRoles(roles);
        return userRepo.save(existingUser);

    }

}
