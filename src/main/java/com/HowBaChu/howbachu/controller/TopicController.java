package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.TopicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/topic")
public class TopicController {

    private final TopicService topicService;

    /*토픽 조회*/
    @GetMapping
    public ResponseEntity<?> getTopic(
        @RequestParam(value = "date", required = false) String date) {
        ResponseCode responseCode = ResponseCode.TOPIC_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(topicService.getTopic(date != null ? LocalDate.parse(date) : null))
            .build(), HttpStatus.OK);
    }

    @GetMapping("/honor")
    public ResponseEntity<?> findHonorTopics() {
        ResponseCode responseCode = ResponseCode.HONOR_TOPICS;
        return responseCode.toResponse(topicService.findHonorTopics());
    }
}
