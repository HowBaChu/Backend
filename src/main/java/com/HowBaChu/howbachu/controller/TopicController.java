package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.service.TopicService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/topic")
public class TopicController {

    private final TopicService topicService;

    /*토픽 조회*/
    @GetMapping
    public ResponseEntity<?> getTopic(@RequestParam(value = "date", required = false) String date) {
        return ResponseCode.TOPIC_SUCCESS.toResponse(
            topicService.getTopicDto(date != null ? LocalDate.parse(date) : null));
    }

    @GetMapping("/honor")
    public ResponseEntity<?> findHonorTopics() {
        return ResponseCode.HONOR_TOPICS.toResponse(topicService.findHonorTopics());
    }
}
