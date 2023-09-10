package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import java.time.LocalDate;
import java.util.List;

public interface TopicService {

    /**
     * 토픽 조회
     */
    Topic getTopic(LocalDate date);

    List<TopicResponseDto> findHonorTopics();
}
