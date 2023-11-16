package com.HowBaChu.howbachu.repository.impl;

import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.VoteRepositoryCustom;
import java.util.Optional;

public class VoteRepositoryImpl extends Querydsl4RepositorySupport implements VoteRepositoryCustom {

    public VoteRepositoryImpl() {
        super(Vote.class);
    }

    @Override
    public boolean findVoteByTopicAndMember(Long topicId, Long memberId) {
        return selectFrom(vote)
            .where(vote.topic.id.eq(topicId).and(vote.member.id.eq(memberId)))
            .fetchOne() != null;
    }

    @Override
    public Optional<Vote> findVoteByEmail(String email) {
        return Optional.ofNullable(
            selectFrom(vote)
                .where(vote.member.email.eq(email))
                .fetchOne()
        );
    }

}
