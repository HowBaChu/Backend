package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.service.VoteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/vote")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<?> voting(@ApiIgnore Authentication authentication,
                                    @RequestBody VoteRequestDto requestDto) {
        return ResponseCode.VOTING_SUCCESS.toResponse(
            voteService.voting(requestDto, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<?> votingStatus(@ApiIgnore Authentication authentication) {
        return ResponseCode.VOTING_SUCCESS.toResponse(voteService.hasVoted(authentication.getName()));
    }

}
