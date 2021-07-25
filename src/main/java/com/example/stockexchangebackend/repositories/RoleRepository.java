package com.example.stockexchangebackend.repositories;

import com.example.stockexchangebackend.models.ERole;
import com.example.stockexchangebackend.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
