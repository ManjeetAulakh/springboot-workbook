package com.pep.taskmanagement.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pep.taskmanagement.exception.RoleException;
import com.pep.taskmanagement.models.Role;
import com.pep.taskmanagement.models.Enums.RoleType;
import com.pep.taskmanagement.repository.RoleRepo;
import com.pep.taskmanagement.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public Role getRoleByName(String name) {
        return roleRepo.findByName(name)
                .orElseThrow(() -> new RoleException("Role does not exist: " + name));
    }

    @Override
    public Role createRole(String name) {
        /*
         * It checks if the input name (which is a String) exactly matches one of the
         * enum constants defined in your RoleType enum.
         */
        
        try {
            Enum.valueOf(RoleType.class, name);
        } catch (IllegalArgumentException e) {
            throw new RoleException("Invalid role name: " + name);
        }

        if (roleRepo.findByName(name).isPresent()) {
            throw new RoleException("Role already exists: " + name);
        }

        Role role = new Role();
        role.setName(name);
        return roleRepo.save(role);
    }

}
