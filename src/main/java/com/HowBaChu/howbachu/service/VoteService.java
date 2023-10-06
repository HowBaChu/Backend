package com.HowBaChu.howbachu.service;


import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import javax.servlet.http.HttpServletResponse;

public interface VoteService {
    Long voting(VoteRequestDto requestDto, String email, HttpServletResponse response);
}
