package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.Vote;

public interface VoteRepositoryCustom {
    Vote fetchVoteStatus(String email, Topic topic);
}
