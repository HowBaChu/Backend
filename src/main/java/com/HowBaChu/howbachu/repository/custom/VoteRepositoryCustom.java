package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.entity.Vote;

public interface VoteRepositoryCustom {
    boolean fetchVoteByTopicAndMember(Long topicId, Long memberId);

    Vote fetchVoteByEmail(String email);
}
