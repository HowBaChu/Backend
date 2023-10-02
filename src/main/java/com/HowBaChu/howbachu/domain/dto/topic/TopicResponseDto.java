package com.HowBaChu.howbachu.domain.dto.topic;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TopicResponseDto {

    private Long id;

    private String title;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate date;

    private SubTitle subTitle;

    private VotingStatus votingStatus;

    public static TopicResponseDto of(Topic topic, VotingStatus votingStatus) {
        return TopicResponseDto.builder()
            .title(topic.getTitle())
            .date(topic.getDate())
            .subTitle(topic.getSubTitle())
            .votingStatus(votingStatus)
            .build();
    }
}
