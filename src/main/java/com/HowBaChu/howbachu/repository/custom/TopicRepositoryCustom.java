package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.entity.Topic;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

public interface TopicRepositoryCustom {
    Topic getTopicByDate(@Nullable LocalDate date);
}
