package com.pep.springjpa.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pep.springjpa.models.User;
import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
}