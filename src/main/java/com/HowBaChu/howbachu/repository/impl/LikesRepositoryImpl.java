package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.entity.Likes;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.LikesRepositoryCustom;

import java.util.Optional;

import static com.HowBaChu.howbachu.domain.entity.QLikes.likes;
import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QOpin.opin;

public class LikesRepositoryImpl extends Querydsl4RepositorySupport implements LikesRepositoryCustom {

    public LikesRepositoryImpl() {
        super(Likes.class);
    }

    @Override
    public Likes fetchLikesByEmailAndOpinId(String email, Long opinId) {
        return Optional.ofNullable(select(likes)
                .from(likes)
                .leftJoin(likes.member, member).fetchJoin()
                .leftJoin(likes.opin, opin).fetchJoin()
                .where(likes.member.email.eq(email).and(likes.opin.id.eq(opinId)))
                .fetchOne())
            .orElse(null);
    }
}
