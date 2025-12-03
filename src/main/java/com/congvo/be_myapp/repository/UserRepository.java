package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);
    User findByEmail(String email);

}
