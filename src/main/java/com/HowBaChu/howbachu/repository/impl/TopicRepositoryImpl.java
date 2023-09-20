package com.HowBaChu.howbachu.repository.impl;

import static com.HowBaChu.howbachu.domain.entity.QTopic.topic;

import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.lang.Nullable;


public class TopicRepositoryImpl extends Querydsl4RepositorySupport implements TopicRepositoryCustom {

    public TopicRepositoryImpl() {
        super(Topic.class);
    }

    @Override
    public Topic getTopicByDate(@Nullable LocalDate date) {
        return Optional.ofNullable(
            selectFrom(topic)
                .where(searchByDate(date))
                .fetchOne()
        ).orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));
    }

    @Override
    public List<TopicResponseDto> getHonorTopic() {
        return selectFrom(topic)
            .orderBy((topic.votingStatus.A.add(topic.votingStatus.B).desc()))
            .limit(20)
            .fetch()
            .stream().map(o -> TopicResponseDto.of(o, o.getVotingStatus()))
            .collect(Collectors.toList());
    }

    private BooleanExpression searchByDate(LocalDate date) {
        return topic.date.eq(Optional.ofNullable(date).orElse(LocalDate.now()));
    }
}
