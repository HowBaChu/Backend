package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.service.TopicService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private VotingStatus votingStatus = new VotingStatus();

    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void votingUpdate() {
        votingStatus = topicRepository.getTopicByDate(LocalDate.now()).getVotingStatus();
        log.info("votingStatus_Updated : {} / {}", votingStatus.getA(), votingStatus.getB());
    }

    @Override
    @Cacheable(value = "topics", key = "#date ?: 'today'")
    public TopicResponseDto getTopic(LocalDate date) {
        return TopicResponseDto.of(topicRepository.getTopicByDate(
            (date != null) ? date : LocalDate.now()), votingStatus);
    }


    @Override
    public List<TopicResponseDto> findHonorTopics() {
        return topicRepository.getHonorTopic();
    }

}
