package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
