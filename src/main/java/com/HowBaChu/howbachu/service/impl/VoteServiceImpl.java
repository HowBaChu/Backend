package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.domain.dto.vote.VoteResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final TopicRepository topicRepository;

    @Override
    public Long voting(VoteRequestDto requestDto, String email, HttpServletResponse response) {
        Topic topic = topicRepository.getTopicByDate(LocalDate.now());
        Member member = memberRepository.findByEmail(email);

        if (hasAlreadyVoted(topic.getId(), member.getId())) {
            throw new CustomException(ErrorCode.VOTE_ALREADY_DONE);
        }

        return voteRepository.save(Vote.of(requestDto, topic, member)).getId();
    }

    @Override
    public VoteResponseDto getVotingStatus(String email) {
        return new VoteResponseDto(voteRepository.fetchVoteByEmail(email).getSelection());
    }

    @Transactional(readOnly = true)
    public boolean hasAlreadyVoted(Long topicId, Long memberId) {
        return voteRepository.fetchVoteByTopicAndMember(topicId, memberId);
    }
}
