package com.HowBaChu.howbachu.utils.quartz;

import com.HowBaChu.howbachu.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@RequiredArgsConstructor
@Slf4j
public class VotingUpdateJob implements Job {

    private final TopicService topicService;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("VotingStatus updated at" + context.getFireTime());
        topicService.votingUpdate();
    }
}
