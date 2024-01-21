package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinThreadResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.TrendingOpinResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.repository.TopicRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.OpinService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpinServiceImpl implements OpinService {

    private final TopicRepository topicRepository;
    private final VoteRepository voteRepository;
    private final OpinRepository opinRepository;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public Long createOpin(OpinRequestDto requestDto, String email, Long parentId) {

        Vote vote = voteRepository.fetchVoteStatus(email, topicRepository.getTopicByDate(LocalDate.now()));

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
    public Page<OpinResponseDto> getOpinList(Pageable pageable, String email) {
        return opinRepository.fetchParentOpinList(pageable, email);
    }

    @Override
    public OpinThreadResponseDto getOpinThread(Long parentId, String email) {
        OpinResponseDto parentOpin = opinRepository.fetchParentOpin(parentId, email);
        List<OpinResponseDto> childOpinList = opinRepository.fetchChildOpinList(parentId, email);
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
        return opinRepository.fetchOpin(opinId, email);
    }

    @Override
    public Page<OpinResponseDto> getMyOpinList(Pageable pageable, String email) {
        return opinRepository.fetchMyOpinList(pageable, email);
    }

    @Override
    @Cacheable(value = "trending", key = "'HotOpin'")
    public TrendingOpinResponseDto getHotOpin() {
        return opinRepository.fetchHotOpin();
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void updateHotOpinCache() {
        try {
            cacheManager.getCache("trending").evict("HotOpin");
            getHotOpin();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.OPIN_HOT_NOT_FOUND);
        }
    }

}
