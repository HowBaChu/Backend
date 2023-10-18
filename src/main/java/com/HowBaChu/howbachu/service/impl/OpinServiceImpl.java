package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinThreadResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.OpinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpinServiceImpl implements OpinService {

    private final VoteRepository voteRepository;
    private final OpinRepository opinRepository;

    @Override
    @Transactional
    public Long createOpin(OpinRequestDto requestDto, String email, Long parentId) {

        Vote vote = voteRepository.findVoteByEmail(email).orElseThrow(
            () -> new CustomException(ErrorCode.VOTE_NOT_FOUND)
        );

        Opin opin;
        if (parentId == null) {
            opin = Opin.of(requestDto.getContent(), vote);
        } else {
            Opin parentOpin = opinRepository.findById(parentId).orElseThrow(
                () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
            );

            opin = Opin.of(requestDto.getContent(), vote, parentOpin);
        }

        return opinRepository.save(opin).getId();
    }

    @Override
    public Page<OpinResponseDto> getOpinList(int page) {
        return opinRepository.fetchParentOpinList(page);
    }

    @Override
    public OpinThreadResponseDto getOpinThread(Long parentId) {
        OpinResponseDto parentOpin = opinRepository.fetchParentOpin(parentId);
        List<OpinResponseDto> childOpinList = opinRepository.fetchOpinChildList(parentId);
        return new OpinThreadResponseDto(parentOpin, childOpinList);
    }

    @Override
    @Transactional
    public Long removeOpin(Long opinId, String email) {
        opinRepository.delete(getOpin(opinId, email));
        return opinId;
    }

    @Override
    @Transactional
    public Long updateOpin(OpinRequestDto requestDto, Long opinId, String email) {
        Opin opin = getOpin(opinId, email);
        opin.updateContent(requestDto.getContent());
        return opinId;
    }

    @Override
    public Opin getOpin(Long opinId, String email) {
        return opinRepository.findByOpinIdAndEmail(opinId, email).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_MISS_MATCH)
        );
    }

    @Override
    public Page<OpinResponseDto> getOpinListForMember(Long memberId, int page) {
        return opinRepository.fetchOpinListByMemberId(memberId, page);
    }

}
