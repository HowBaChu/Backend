package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final TopicRepository topicRepository;
    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;

    @Override
    public Long voting(VoteRequestDto requestDto, Principal principal) {
        return voteRepository.save(Vote.of(
            requestDto,
            topicRepository.getTopic(LocalDate.now()),
            memberRepository.findByEmail(principal.getName())))
            .getId();
    }

}
