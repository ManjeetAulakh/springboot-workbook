package com.pep.taskmanagement.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pep.taskmanagement.models.Role;
import com.pep.taskmanagement.models.Enums.RoleType;
import com.pep.taskmanagement.repository.RoleRepo;

import jakarta.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepo roleRepo;

    @PostConstruct
    public void initRoles() {
        for (RoleType type : RoleType.values()) {
            roleRepo.findByName(type.name()).orElseGet(() -> {
                Role role = new Role();
                role.setName(type.name());
                return roleRepo.save(role);
            });
        }
    }
}
