package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;

import java.util.List;

public interface OpinService {
    Long createOpin(OpinRequestDto requestDto, String email, Long parentId);
    List<OpinResponseDto> getOpinList();
    List<OpinResponseDto> getOpinChildList(Long parentId);
    Long removeOpin(Long opinId, String email);
    Long updateOpin(OpinRequestDto requestDto, Long opinId, String email);
}
