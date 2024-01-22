package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinThreadResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.TrendingOpinResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpinService {
    Long createOpin(OpinRequestDto requestDto, String email, Long parentId);

    Page<OpinResponseDto> getOpinList(Pageable pageable, String email);

    OpinThreadResponseDto getOpinThread(Long parentId, String email);

    Long removeOpin(Long opinId, String email);

    Long updateOpin(OpinRequestDto requestDto, Long opinId, String email);

    Opin getOpin(Long opinId, String email);

    Page<OpinResponseDto> getMyOpinList(Pageable pageable, String email);

    TrendingOpinResponseDto getHotOpin();
}
