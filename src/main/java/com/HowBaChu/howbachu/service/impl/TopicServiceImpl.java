package com.HowBaChu.howbachu.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.service.TopicService;
import com.HowBaChu.howbachu.utils.quartz.VotingUpdateJob;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private VotingStatus votingStatus = new VotingStatus();

    @PostConstruct
    public void init() throws SchedulerException {
        Scheduler schedule = new StdSchedulerFactory().getScheduler();

        JobDetail votingUpdateJob = newJob(VotingUpdateJob.class)
            .withIdentity("votingUpdate", "vote")
            .build();

        CronTrigger votingUpdateTrigger = newTrigger().withIdentity("votingTrigger","vote")
            .withSchedule(cronSchedule("0 * * * * ?"))
            .build();

        schedule.scheduleJob(votingUpdateJob, votingUpdateTrigger);
        schedule.start();
    }

    @Override
    public void votingUpdate() {
        votingStatus = topicRepository.getTopicByDate(LocalDate.now()).getVotingStatus();
    }

    @Override
    public TopicResponseDto getTopic(LocalDate date) {
        return TopicResponseDto.of(topicRepository.getTopicByDate(date),votingStatus);
    }

    @Override
    public List<TopicResponseDto> findHonorTopics() {
        return topicRepository.getHonorTopic();
    }

}
