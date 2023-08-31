package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.entity.Topic;

import java.time.LocalDate;

public interface TopicService {

    /**
     * 토픽 조회
     */
    Topic getTopic(LocalDate date);

}
