package com.HowBaChu.howbachu.repository.custom;


public interface VoteRepositoryCustom {
    boolean findVoteByTopicAndMember(Long topicId, Long memberId);
}
