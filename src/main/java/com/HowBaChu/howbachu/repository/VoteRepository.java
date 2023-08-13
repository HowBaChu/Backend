package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.repository.custom.VoteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {
}
