package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private VotingStatus votingStatus = new VotingStatus();

    @Override
    public TopicResponseDto getTopicDto(LocalDate date) {
        return TopicResponseDto.of(topicRepository.getTopicByDate((date != null) ? date : LocalDate.now()), votingStatus);
    }

    @Override
    @Scheduled(cron = "0 0 * * * ?")
    public void votingUpdate() {
        votingStatus = topicRepository.getTopicByDate(LocalDate.now()).getVotingStatus();
        log.info("votingStatus_Updated : {} / {}", votingStatus.getA(), votingStatus.getB());
    }

    @Override
    public List<TopicResponseDto> findHonorTopics() {
        return topicRepository.getHonorTopic();
    }

}
