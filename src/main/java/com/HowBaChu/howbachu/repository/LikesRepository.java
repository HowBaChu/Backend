package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findLikesByMember_EmailAndOpin_Id(String email, Long opinId);

    boolean existsByMember_EmailAndOpin_Id(String email, Long opinId);
}
