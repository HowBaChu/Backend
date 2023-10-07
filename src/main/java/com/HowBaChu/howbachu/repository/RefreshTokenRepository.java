package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByValue(String email);

    void deleteByValue(String email);
}
