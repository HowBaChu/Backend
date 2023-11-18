package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Likes;
import com.HowBaChu.howbachu.repository.custom.LikesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long>, LikesRepositoryCustom {
}
