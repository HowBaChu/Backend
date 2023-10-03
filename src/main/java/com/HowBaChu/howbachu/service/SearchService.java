package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    SearchResultResponseDto fetchSearchResult(String condition);
    Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchMoreParentOpins(String condition, Pageable pageable);
    Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchMoreChildOpins(String condition, Pageable pageable);
    Page<SearchResultResponseDto.TopicSearchResponseDto> fetchMoreTopics(String condition, Pageable pageable);
}
