package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.VoteRepositoryCustom;

import java.util.Optional;

import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

public class VoteRepositoryImpl extends Querydsl4RepositorySupport implements VoteRepositoryCustom {

    public VoteRepositoryImpl() {
        super(Vote.class);
    }

    @Override
    public boolean fetchVoteByTopicAndMember(Long topicId, Long memberId) {
        return Optional.ofNullable(selectFrom(vote)
                .where(vote.topic.id.eq(topicId).and(vote.member.id.eq(memberId)))
                .fetchOne())
            .isPresent();
    }

    @Override
    public Vote fetchVoteByEmail(String email) {
        return Optional.ofNullable(
            selectFrom(vote)
                .leftJoin(vote.member, member).fetchJoin()
                .where(member.email.eq(email))
                .fetchOne()
        ).orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
    }

}
