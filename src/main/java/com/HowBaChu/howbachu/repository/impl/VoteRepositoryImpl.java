package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.VoteRepositoryCustom;

import static com.HowBaChu.howbachu.domain.entity.QMember.member;
import static com.HowBaChu.howbachu.domain.entity.QVote.vote;

public class VoteRepositoryImpl extends Querydsl4RepositorySupport implements VoteRepositoryCustom {

    public VoteRepositoryImpl() {
        super(Vote.class);
    }

    @Override
    public Vote fetchVoteStatus(String email, Topic topic) {
        return selectFrom(vote)
            .leftJoin(vote.member, member).fetchJoin()
            .where(vote.member.email.eq(email)
                .and(vote.topic.id.eq(topic.getId())))
            .fetchOne();
    }


}
