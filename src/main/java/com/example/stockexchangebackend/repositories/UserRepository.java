package com.example.stockexchangebackend.repositories;


import com.example.stockexchangebackend.models.User1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User1,Long> {
    @Query("SELECT U FROM User1 U WHERE U.username=:username AND U.password=:password")
    User1 getByNameAndPassword(String username,String password);

    Optional<User1> findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
