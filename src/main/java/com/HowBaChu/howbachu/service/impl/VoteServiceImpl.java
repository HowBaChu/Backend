package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.VoteService;
import com.HowBaChu.howbachu.utils.CookieUtil;
import java.time.LocalDate;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final MemberRepository memberRepository;
    private final VoteRepository voteRepository;
    private final TopicServiceImpl topicService;
    private final CookieUtil cookieUtil;

    @Override
    public Long voting(VoteRequestDto requestDto, String email, HttpServletResponse response) {
        Topic topic = topicService.getTopic(LocalDate.now());
        Member member = memberRepository.findByEmail(email);

        if (hasAlreadyVoted(topic.getId(), member.getId())) {
            throw new CustomException(ErrorCode.VOTE_ALREADY_DONE);
        }

        cookieUtil.setCookie(response, "Vote", requestDto.getSelection().toString(), cookieUtil.getRemainTime());
        return voteRepository.save(Vote.of(requestDto, topic, member)).getId();
    }

    @Transactional(readOnly = true)
    public boolean hasAlreadyVoted(Long topicId, Long memberId) {
        return voteRepository.findVoteByTopicAndMember(topicId, memberId);
    }
}
