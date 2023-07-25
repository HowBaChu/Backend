package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByEmail(String email);

    boolean existsByUsername(String nickname);
}
