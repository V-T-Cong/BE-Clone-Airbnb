package com.congvo.be_myapp.repository;

import com.congvo.be_myapp.entity.PasswordResetToken;
import com.congvo.be_myapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    @Modifying
    void deleteByUser(User user);

}
