package com.HowBaChu.howbachu.repository.custom;

import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.TrendingOpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;

import java.util.List;

import com.HowBaChu.howbachu.domain.entity.Opin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpinRepositoryCustom {

    /* 단건 Opin 조회 - 삭제용(opinId, email )*/
    Opin fetchOpin(Long opinId, String email);

    /* 부모 댓글 검색 결과 */
    Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, String email, Pageable pageable);

    /* 자식 댓글 검색 결과 */
    Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, String email, Pageable pageable);

    /* 자식 댓글 리스트 조회 */
    List<OpinResponseDto> fetchChildOpinList(Long parentId, String email);

    /* 부모 댓글 단건 조회 */
    OpinResponseDto fetchParentOpin(Long opinId, String email);

    /* 댓글 조회 */
    Page<OpinResponseDto> fetchParentOpinList(Pageable pageable, String email);

    /* 특정 회원이 쓴 댓글 조회 -> 내가 쓴 댓글 포함 */
    Page<OpinResponseDto> fetchMyOpinList(Pageable pageable, String email);

    /* 지난 1시간 동안 가장 핫한 게시글 조회 */
    TrendingOpinResponseDto fetchHotOpin();
}
