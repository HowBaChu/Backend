package com.HowBaChu.howbachu.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.service.TopicService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(TopicController.class)
public class TopicControllerTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TopicService topicService;


    @Test
    @WithMockUser(username = "user")
    @DisplayName("[GET] 토픽 조회 테스트 - date : X")
    public void getTopicWithNullDateTest() throws Exception {

        // given
        Topic topic = sample();
        given(topicService.getTopicDto(null)).willReturn(TopicResponseDto.of(topic, new VotingStatus()));

        // when & then
        mockMvc.perform(get("/api/v1/topic")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").exists())
            .andExpect(jsonPath("$.data.date").exists())
            .andExpect(jsonPath("$.data.topicSubTitle").exists())
            .andExpect(jsonPath("$.data.votingStatus").exists())
            .andDo(print());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("[GET] 토픽 조회 테스트 - date : O")
    public void getTopicWithSpecificDateTest() throws Exception {

        // given
        LocalDate today = LocalDate.of(2023, 8, 12);
        Topic topic = sample();
        given(topicService.getTopicDto(today)).willReturn(TopicResponseDto.of(topic, new VotingStatus()));

        // when & then
        mockMvc.perform(get("/api/v1/topic")
                .param("date", today.toString())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.title").exists())
            .andExpect(jsonPath("$.data.date").exists())
            .andExpect(jsonPath("$.data.topicSubTitle").exists())
            .andExpect(jsonPath("$.data.votingStatus").exists())
            .andDo(print());
    }

    private static Topic sample() {
        return Topic.builder()
            .id(1L)
            .title("탕수육은 부먹 vs 찍먹")
            .date(LocalDate.now())
            .subTitle(new SubTitle("탕수육은 원래 부어먹는 음식이다", "찍먹이 진리다 바삭바삭"))
            .votingStatus(new VotingStatus())
            .build();
    }


}
