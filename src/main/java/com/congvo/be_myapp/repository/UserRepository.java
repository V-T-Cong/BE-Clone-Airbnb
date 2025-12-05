package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);

    User findById(UUID id);
    User findByEmail(String email);

}
