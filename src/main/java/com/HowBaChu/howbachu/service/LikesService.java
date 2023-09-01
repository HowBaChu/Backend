package com.HowBaChu.howbachu.service;

public interface LikesService {
    Long addLikes(String email, Long opineId);
    Long cancelLikes(String email, Long opineId);
}
