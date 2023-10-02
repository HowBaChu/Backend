package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final OpinRepository opinRepository;
    private final TopicRepository topicRepository;

    @Override
    public SearchResultResponseDto fetchSearchResult(String condition) {
        return new SearchResultResponseDto(
            opinRepository.fetchParentOpinSearch(condition),
            opinRepository.fetchChildOpinSearch(condition),
            topicRepository.fetchTopicSearch(condition)
        );
    }

    @Override
    public Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchMoreParentOpins(String condition, Pageable pageable) {
        return opinRepository.fetchParentOpinSearch(condition, pageable);
    }

    @Override
    public Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchMoreChildOpins(String condition, Pageable pageable) {
        return opinRepository.fetchChildOpinSearch(condition, pageable);
    }

    @Override
    public Page<SearchResultResponseDto.TopicSearchResponseDto> fetchMoreTopics(String condition, Pageable pageable) {
        return topicRepository.fetchTopicSearch(condition, pageable);
    }

}
