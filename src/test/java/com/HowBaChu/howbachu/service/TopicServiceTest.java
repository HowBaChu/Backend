package com.HowBaChu.howbachu.service;

import static org.mockito.BDDMockito.given;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import com.HowBaChu.howbachu.service.impl.TopicServiceImpl;
import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicRepositoryCustom topicRepositoryCustom;

    @InjectMocks
    private TopicServiceImpl topicService;

//    @Test
//    public void 토픽_조회() {
//
//        // given
//        Topic expectedTopic = Topic.builder()
//            .id(1L)
//            .title("부먹이냐 찍먹이냐")
//            .date(LocalDate.now())
//            .subTitle(new SubTitle("부먹이다", "찍먹이다"))
//            .votingStatus(new VotingStatus())
//            .build();
//
//        given(topicService.getTopicDto(null)).willReturn(expectedTopic);
//        given(topicService.getTopicDto(LocalDate.now())).willReturn(expectedTopic);
//
//        // when
//        Topic dateIsNullTopic = topicService.getTopicDto(null);
//        Topic dateIsTodayTopic = topicService.getTopicDto(LocalDate.now());
//
//        // then
//        Assertions.assertThat(dateIsNullTopic.getId()).isEqualTo(1L);
//        Assertions.assertThat(dateIsTodayTopic.getId()).isEqualTo(1L);
//
//        // null 값 유무에 따라서
//        System.out.println(dateIsNullTopic.equals(dateIsTodayTopic));
//    }
}
