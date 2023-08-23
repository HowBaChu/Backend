package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpinRepository extends JpaRepository<Opin, Long> {

    Optional<Opin> findById(Long id);

    @Query("SELECT new com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto" +
        "(o.id, v.selectSubTitle, m.username, o.content, o.likeCnt) " +
        "FROM Opin o LEFT JOIN o.vote v LEFT JOIN v.member m")
    List<OpinResponseDto> fetchOpinList();

    @Query("SELECT new com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto" +
        "(o.id, v.selectSubTitle, m.username, o.content, o.likeCnt) " +
        "FROM Opin o LEFT JOIN o.vote v LEFT JOIN v.member m ON o.parent.id =: parentId")
    List<OpinResponseDto> fetchOpinChildList(@Param("parentId") Long parentId);

    @Query("SELECT o from Opin o JOIN FETCH o.vote v JOIN FETCH v.member m where o.id =: id and m.email =: email")
    Optional<Opin> findByOpinIdAndEmail(@Param("id") Long opinId, @Param("email") String email);

}
