package com.congvo.be_myapp.service;

import com.congvo.be_myapp.entity.RefreshToken;
import com.congvo.be_myapp.entity.User;
import com.congvo.be_myapp.repository.RefreshTokenRepository;
import com.congvo.be_myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${security.jwt.refresh-expiration-time}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public String getRefreshToken(UUID userId) {
        User user = userRepository.findById(userId);

        // SỬA ĐỔI: Kiểm tra xem user đã có refresh token chưa
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> {
                    RefreshToken newToken = new RefreshToken();
                    newToken.setUser(user);
                    return newToken;
                });

        // Cập nhật lại token mới và hạn sử dụng
        String token = UUID.randomUUID().toString();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(token);

        // JPA sẽ tự động Update nếu object đã có ID, hoặc Insert nếu chưa có ID
        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signing request");
        }
        return token;
    }

}
