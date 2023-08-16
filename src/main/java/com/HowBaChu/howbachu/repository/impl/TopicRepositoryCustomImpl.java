package com.HowBaChu.howbachu.repository.impl;

import static com.HowBaChu.howbachu.domain.entity.QTopic.topic;

import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.Optional;


public class TopicRepositoryCustomImpl extends Querydsl4RepositorySupport implements TopicRepositoryCustom {

    public TopicRepositoryCustomImpl() {
        super(Topic.class);
    }

    @Override
    public Topic getTopic(LocalDate date) {
        return Optional.ofNullable(
            selectFrom(topic)
                .where(searchByDate(date))
                .fetchOne()
        ).orElseThrow(() -> new CustomException(ErrorCode.TOPIC_NOT_FOUND));
    }

    private BooleanExpression searchByDate(LocalDate date) {
        return Optional.ofNullable(date).isPresent() ? topic.date.eq(date) : topic.date.eq(LocalDate.now());
    }
}
