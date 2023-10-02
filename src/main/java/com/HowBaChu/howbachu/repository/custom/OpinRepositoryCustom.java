package com.HowBaChu.howbachu.repository.custom;


public interface OpinRepositoryCustom {
    /* 부모 댓글 검색 결과 */
    List<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition);
    /* 자식 댓글 검색 결과 */
    List<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition);
     /* 부모 댓글 검색 결과 - 페이징 적용 <더보기> */
    Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, Pageable pageable);
    /* 자식 댓글 검색 결과 - 페이징 적용 <더보기> */
    Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, Pageable pageable);
}
