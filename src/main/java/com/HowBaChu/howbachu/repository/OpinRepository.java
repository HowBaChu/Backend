package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.repository.custom.OpinRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OpinRepository extends JpaRepository<Opin, Long>, OpinRepositoryCustom {

    Optional<Opin> findById(Long id);

    @Query("SELECT o from Opin o JOIN FETCH o.vote v JOIN FETCH v.member m WHERE o.id = :id AND m.email = :email")
    Optional<Opin> findByOpinIdAndEmail(@Param("id") Long opinId, @Param("email") String email);
}
