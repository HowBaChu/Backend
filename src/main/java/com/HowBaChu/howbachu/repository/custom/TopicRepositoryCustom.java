package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.domain.dto.topic.TopicResponseDto;
import com.HowBaChu.howbachu.domain.entity.Topic;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface TopicRepositoryCustom {
    Topic getTopicByDate(@Nullable LocalDate date);
    List<TopicResponseDto> getHonorTopic();
    List<SearchResultResponseDto.TopicSearchResponseDto> fetchTopicSearch(String condition);
    Page<SearchResultResponseDto.TopicSearchResponseDto> fetchTopicSearch(String condition, Pageable pageable);
}
