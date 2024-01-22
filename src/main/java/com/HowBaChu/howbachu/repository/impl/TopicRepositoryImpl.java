package com.HowBaChu.howbachu.repository.impl;

import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.Support.Querydsl4RepositorySupport;
import com.HowBaChu.howbachu.repository.custom.TopicRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.HowBaChu.howbachu.domain.entity.QTopic.topic;


public class TopicRepositoryImpl extends Querydsl4RepositorySupport implements TopicRepositoryCustom {

    public TopicRepositoryImpl() {
        super(Topic.class);
    }

    @Override
    @Cacheable(value = "topic", key = "#date")
    public Topic getTopicByDate(@Nullable LocalDate date) {
        return Optional.ofNullable(
            selectFrom(topic)
                .where(topic.date.eq(Optional.ofNullable(date).orElse(LocalDate.now())))
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

    @Override
    public List<SearchResultResponseDto.TopicSearchResponseDto> fetchTopicSearch(String condition) {
        Pageable pageable = PageRequest.of(0, 5);
        return select(mapToTopicSearchResponseDto())
            .from(topic)
            .where(topic.title.contains(condition)
                .or(topic.subTitle.sub_A.contains(condition))
                .or(topic.subTitle.sub_B.contains(condition)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(topic.date.desc())
            .fetch();
    }

    @Override
    public Page<SearchResultResponseDto.TopicSearchResponseDto> fetchTopicSearch(String condition, Pageable pageable) {
        List<SearchResultResponseDto.TopicSearchResponseDto> content =
            select(mapToTopicSearchResponseDto())
                .from(topic)
                .where(topic.title.contains(condition)
                    .or(topic.subTitle.sub_A.contains(condition))
                    .or(topic.subTitle.sub_B.contains(condition)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(topic.date.desc())
                .fetch();

        JPAQuery<Long> countQuery = select(topic.count())
            .from(topic)
            .where(topic.title.contains(condition)
                .or(topic.subTitle.sub_A.contains(condition))
                .or(topic.subTitle.sub_B.contains(condition)));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }


    private static QBean<SearchResultResponseDto.TopicSearchResponseDto> mapToTopicSearchResponseDto() {
        return Projections.fields(SearchResultResponseDto.TopicSearchResponseDto.class,
            topic.id.as("topicId"),
            topic.title.as("title"),
            Projections.fields(SubTitle.class,
                topic.subTitle.sub_A.as("sub_A"),
                topic.subTitle.sub_B.as("sub_B")
            ).as("subTitle"),
            Projections.fields(VotingStatus.class,
                topic.votingStatus.A.as("A"),
                topic.votingStatus.B.as("B")
            ).as("votingStatus"),
            topic.date.as("date")
        );
    }

}
