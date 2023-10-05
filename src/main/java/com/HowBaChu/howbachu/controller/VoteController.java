package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
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

        ResponseCode responseCode = ResponseCode.VOTING_SUCCESS;
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(responseCode)
            .code(responseCode.getCode())
            .message(responseCode.getMessage())
            .data(voteService.voting(requestDto, authentication.getName()))
            .build(), responseCode.getHttpStatus());
    }

}
