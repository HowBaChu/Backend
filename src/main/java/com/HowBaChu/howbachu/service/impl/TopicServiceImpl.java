package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    @Override
    public Topic getTopic(LocalDate date) {
        return topicRepository.getTopicByDate(date);
    }
}
