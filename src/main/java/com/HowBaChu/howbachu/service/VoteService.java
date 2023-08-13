package com.HowBaChu.howbachu.service;


import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;

import java.security.Principal;

public interface VoteService {
    Long voting(VoteRequestDto requestDto, Principal principal);
}
