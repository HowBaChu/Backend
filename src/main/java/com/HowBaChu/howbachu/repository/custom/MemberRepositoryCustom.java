package com.HowBaChu.howbachu.repository.custom;

import com.HowBaChu.howbachu.domain.entity.Member;

public interface MemberRepositoryCustom {
    Member findByEmail(String email);
}
