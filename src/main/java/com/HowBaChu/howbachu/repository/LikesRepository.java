package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Likes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findLikesByMember_EmailAndOpin_Id(String email, Long opinId);

    boolean existsByMember_EmailAndOpin_Id(String email, Long opinId);
}
