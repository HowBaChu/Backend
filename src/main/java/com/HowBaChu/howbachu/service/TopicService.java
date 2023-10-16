package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface TopicService {
    TopicResponseDto getTopicDto(LocalDate date);
    List<TopicResponseDto> findHonorTopics();
    void votingUpdate();
}
