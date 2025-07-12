package com.pep.taskmanagement.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pep.taskmanagement.Dtos.UserDTO;
import com.pep.taskmanagement.models.Role;
import com.pep.taskmanagement.models.User;
import com.pep.taskmanagement.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private ModelMapper modelMapper;

    public UserDTO mapToDto(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);

        // manually map roles since it's a Set<Role> → Set<String>
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

        return dto;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> savedUsers = userService.getAllUsers();
        List<UserDTO> dtoList = savedUsers.stream().map(this::mapToDto).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getByID(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(mapToDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(mapToDto(createdUser));
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<UserDTO> updateUserRoles(@PathVariable Long id,
            @RequestBody(required = false) Map<String, Set<String>> rolesMap) {

        Set<String> roleNames;

        if (rolesMap == null || !rolesMap.containsKey("roles") || rolesMap.get("roles").isEmpty()) {
            roleNames = Set.of("ROLE_TEAM_MEMBER");
        } else {
            roleNames = rolesMap.get("roles");
        }

        User updatedUser = userService.updateRoles(id, roleNames);
        return ResponseEntity.ok().body(mapToDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
