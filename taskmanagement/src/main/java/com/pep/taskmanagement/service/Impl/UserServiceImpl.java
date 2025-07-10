package com.pep.taskmanagement.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.pep.taskmanagement.exception.RoleNotFoundException;
import com.pep.taskmanagement.exception.UserAlreadyExistsException;
import com.pep.taskmanagement.exception.UserNotFoundException;
import com.pep.taskmanagement.models.Role;
import com.pep.taskmanagement.models.User;
import com.pep.taskmanagement.repository.RoleRepo;
import com.pep.taskmanagement.repository.UserRepo;
import com.pep.taskmanagement.service.UserService;

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
            throw new UserAlreadyExistsException("User already exists with username: " + user.getUsername());
        }

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }

        // set encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set deafult roles
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole  = roleRepo.findByName("ROLE_TEAM-MEMBER")
                    .orElseThrow(() -> new RoleNotFoundException("Role is not exists : ROLE_TEAM-MEMBER"));
            roles.add(defaultRole);
    
        } else {
            for(Role role : user.getRoles()) {
                Role dbRole = roleRepo.findByName(role.getName())
                    .orElseThrow(() -> new RoleNotFoundException("Role is not exists : "  + role.getName()));
                roles.add(dbRole);
            }
        }
        user.setRoles(roles);

        return userRepo.save(user);
    }

    @Override
    public User getUserById(long userId) {
        return userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with this User ID: " + userId)
        );
    }

    @Override
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with this User ID: " + userId)
        );
        
        userRepo.delete(user);
    }

    @Override
    public User updateRoles(long userId, String roleName) {

        User existingUser = userRepo.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with this User ID: " + userId)
        );

        Set<Role> roles = new HashSet<>();
        
        if(roleName == "manager"){
            Role existingRole = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role is not supported" + roleName));
            roles.add(existingRole);

        }else if(roleName == "member"){
            Role existingRole = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role is not supported" + roleName));
            roles.add(existingRole);

        }else{
            throw new RoleNotFoundException("Role is not supported" + roleName);
        }

        existingUser.setRoles(roles);
        return userRepo.save(existingUser);
 
    }

}
