package com.youcode.taskmanager.core.database.repository;

import com.youcode.taskmanager.core.database.model.entity.RefreshToken;
import com.youcode.taskmanager.core.database.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}