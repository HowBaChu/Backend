package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.TopicSubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import com.HowBaChu.howbachu.service.impl.TopicServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicRepositoryCustom topicRepositoryCustom;

    @InjectMocks
    private TopicServiceImpl topicService;

    @Test
    public void 토픽_조회() {

        // given
        Topic expectedTopic = Topic.builder()
            .id(1L)
            .title("부먹이냐 찍먹이냐")
            .date(LocalDate.now())
            .topicSubTitle(new TopicSubTitle("부먹이다", "찍먹이다"))
            .votingStatus(new VotingStatus())
            .build();

        given(topicService.getTopic(null)).willReturn(expectedTopic);
        given(topicService.getTopic(LocalDate.now())).willReturn(expectedTopic);

        // when
        Topic dateIsNullTopic = topicService.getTopic(null);
        Topic dateIsTodayTopic = topicService.getTopic(LocalDate.now());

        // then
        Assertions.assertThat(dateIsNullTopic.getId()).isEqualTo(1L);
        Assertions.assertThat(dateIsTodayTopic.getId()).isEqualTo(1L);

        // null 값 유무에 따라서
        System.out.println(dateIsNullTopic.equals(dateIsTodayTopic));
    }
}
