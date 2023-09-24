package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
