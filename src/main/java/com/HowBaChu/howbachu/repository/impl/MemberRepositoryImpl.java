package com.HowBaChu.howbachu.repository.impl;

import static com.HowBaChu.howbachu.domain.entity.QMember.member;

import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.MemberRepositoryCustom;
import java.util.Optional;

public class MemberRepositoryImpl extends Querydsl4RepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Member findByEmail(String email) {
        return Optional.ofNullable(
                selectFrom(member)
                .where(member.email.eq(email))
                .fetchFirst()
        ).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
