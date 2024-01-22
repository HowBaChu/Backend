package com.HowBaChu.howbachu.service;


import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.domain.dto.vote.VoteResponseDto;

public interface VoteService {
    Long voting(VoteRequestDto requestDto, String email);
    VoteResponseDto hasVoted(String name);
}
