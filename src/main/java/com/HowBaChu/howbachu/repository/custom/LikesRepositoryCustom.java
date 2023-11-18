package com.HowBaChu.howbachu.repository.custom;

import com.HowBaChu.howbachu.domain.entity.Likes;

public interface LikesRepositoryCustom {
    Likes fetchLikesByEmailAndOpinId(String email, Long opinId);
}
