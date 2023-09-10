package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import java.util.List;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public interface TopicRepositoryCustom {
    Topic getTopicByDate(@Nullable LocalDate date);

    List<TopicResponseDto> getHonorTopic();
}
