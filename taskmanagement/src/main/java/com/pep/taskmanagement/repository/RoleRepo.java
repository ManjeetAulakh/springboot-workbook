package com.pep.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pep.taskmanagement.models.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    
    Optional<Role> findByName(String name);
    boolean existsByName(String name);

}
