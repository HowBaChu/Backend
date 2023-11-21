package com.HowBaChu.howbachu.service;

public interface LikesService {
    Long addLikes(String email, Long opinId);

    Long cancelLikes(String email, Long opinId);

    boolean checkDuplicateLike(String email, Long opinId);
}
