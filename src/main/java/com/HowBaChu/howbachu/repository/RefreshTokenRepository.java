package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByKey(String email);

    void deleteByKey(String email);
}
