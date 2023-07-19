package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    String findByKey(String email);
}
