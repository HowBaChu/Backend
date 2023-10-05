package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<?> search(@Nullable @RequestParam("cond") String cond) {
        ResponseCode responseCode = ResponseCode.SEARCH_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(searchService.fetchSearchResult(cond))
            .build(), responseCode.getHttpStatus());
    }

    @GetMapping(value = "/parent-opin/")
    public ResponseEntity<?> searchMoreParentOpin(@Nullable @RequestParam("cond") String cond, Pageable pageable) {
        ResponseCode responseCode = ResponseCode.SEARCH_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(searchService.fetchMoreParentOpins(cond, pageable))
            .build(), responseCode.getHttpStatus());
    }

    @GetMapping(value = "/child-opin/")
    public ResponseEntity<?> searchMoreChildOpin(@Nullable @RequestParam("cond") String cond, Pageable pageable) {
        ResponseCode responseCode = ResponseCode.SEARCH_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(searchService.fetchMoreChildOpins(cond, pageable))
            .build(), responseCode.getHttpStatus());
    }

    @GetMapping(value = "/topic/")
    public ResponseEntity<?> searchMoreTopic(@Nullable @RequestParam("cond") String cond, Pageable pageable) {
        ResponseCode responseCode = ResponseCode.SEARCH_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(searchService.fetchMoreTopics(cond, pageable))
            .build(), responseCode.getHttpStatus());
    }



}
