package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@Nullable @RequestParam("cond") String cond) {
        return ResponseCode.SEARCH_SUCCESS.toResponse(searchService.fetchSearchResult(cond));
    }

    @GetMapping(value = "/parent-opin/")
    public ResponseEntity<?> searchMoreParentOpin(@Nullable @RequestParam("cond") String cond,
        Pageable pageable) {
        return ResponseCode.SEARCH_SUCCESS.toResponse(
            searchService.fetchMoreParentOpins(cond, pageable));
    }

    @GetMapping(value = "/child-opin/")
    public ResponseEntity<?> searchMoreChildOpin(@Nullable @RequestParam("cond") String cond,
        Pageable pageable) {
        return ResponseCode.SEARCH_SUCCESS.toResponse(
            searchService.fetchMoreChildOpins(cond, pageable));
    }

    @GetMapping(value = "/topic/")
    public ResponseEntity<?> searchMoreTopic(@Nullable @RequestParam("cond") String cond,
        Pageable pageable) {
        return ResponseCode.SEARCH_SUCCESS.toResponse(
            searchService.fetchMoreTopics(cond, pageable));
    }

}
