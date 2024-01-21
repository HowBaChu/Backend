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

    /**
     * 투표 정보 저장.
     *
     * @param requestDto        : 투표 요청 데이터
     * @param email             : 사용자 이메일
     * @return                  : 저장된 투표의 ID
     * @throws CustomException  : 이미 투표를 완료한 경우
     */
    @Override
    public Long voting(VoteRequestDto requestDto, String email) {

        /* 사용자의 이메일을 통해 회원 정보를 조회 */
        Member member = memberRepository.findByEmail(email);

        /* 현재 날짜에 해당하는 주제 가져오기  */
        Topic topic = topicRepository.getTopicByDate(LocalDate.now());

        /* 이미 투표를 완료한 경우, 예외 발생 */
        if (voteRepository.fetchVoteStatus(email, topic) != null) {
            throw new CustomException(ErrorCode.VOTE_ALREADY_DONE);
        }

        /* 투표를 저장하고 저장된 투표의 ID를 반환 */
        return voteRepository.save(Vote.of(requestDto, topic, member)).getId();
    }

    /**
     * 사용자가 이미 투표했는지 확인.
     *
     * @param email             : 사용자 이메일
     * @return                  : 투표 상태
     */
    @Override
    public VoteResponseDto hasVoted(String email) {

        /* 사용자의 이메일을 통해 회원의 투표 상태 확인 */
        Vote vote = voteRepository.fetchVoteStatus(email, topicRepository.getTopicByDate(LocalDate.now()));

        /* 투표 상태를 VoteResponseDto 객체로 반환 */
        return new VoteResponseDto(vote.getSelection());
    }


}
