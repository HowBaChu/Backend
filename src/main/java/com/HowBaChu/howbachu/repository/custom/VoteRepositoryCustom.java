package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.entity.Vote;

import java.util.Optional;

public interface VoteRepositoryCustom {
    boolean findVoteByTopicAndMember(Long topicId, Long memberId);

    Optional<Vote> findVoteByEmail(String email);
}
