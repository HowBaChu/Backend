package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.entity.Topic;

import java.time.LocalDate;

public interface TopicRepositoryCustom {
    Topic getTopic(LocalDate date);
}
