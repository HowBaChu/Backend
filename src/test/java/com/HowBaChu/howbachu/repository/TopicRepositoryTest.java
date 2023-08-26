package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
@ActiveProfiles(value = "test")
class TopicRepositoryTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    TopicRepository topicRepository;

    public void init() {
        topicRepository.save(Topic.builder()
            .title("탕수육은 부먹 or 찍먹")
            .subTitle(new SubTitle("탕수육이 찍먹이다", "탕수육은 원래 부먹이다"))
            .votingStatus(new VotingStatus())
            .build());
    }

    @Test
    public void TopicCreateTest() {

        // given
        Topic topic = Topic.builder()
            .title("짜장® vs 짬뽕")
            .subTitle(new SubTitle("짜장면이 진리다", "국물이 진리다. 짬뽕!"))
            .votingStatus(new VotingStatus())
            .build();

        // when
        Topic savedTopic = topicRepository.save(topic);

        // then
        Assertions.assertThat(savedTopic.getId()).isEqualTo(topic.getId());
    }



}