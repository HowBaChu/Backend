package com.HowBaChu.howbachu.domain.dto.search;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponseDto {

    List<ParentsOpinSearchResponseDto> parentsOpinList;
    List<ChildOpinSearchResponseDto> childOpinList;
    List<TopicSearchResponseDto> topicList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* P-Opin 검색 결과 */
    public static class ParentsOpinSearchResponseDto {

        /* 댓글 작성자 프로필 이미지 */
        private String profileImg;

        /* 댓글 작성자 */
        private String author;

        /* 댓글 내용 */
        private String content;

        /* 좋아요 갯수 */
        private int likeCnt;

        /* 투표한 부제 */
        private String topicSubtitle;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* Ch-Opin 검색 결과 */
    public static class ChildOpinSearchResponseDto {

        /* 댓글 작성자 프로필 이미지 */
        private String profileImg;

        /* 댓글 작성자 */
        private String author;

        /* 댓글 내용 */
        private String content;

        /* 좋아요 갯수 */
        private int likeCnt;

        /* 투표한 부제 */
        private String topicSubtitle;

        /* 부모 댓글 Id */
        private Long parentOpinId;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* Topic 검색 결과 */
    public static class TopicSearchResponseDto {

        /* 토픽 제목 */
        private String title;

        /* 토픽 부제목(sub_A, sub_B) */
        private String subTitleA;
        private String subTitleB;

        /* 투표 현황(A, B) */
        private int countA;
        private int countB;

        /* 토픽 날짜(MM:dd) */
        private LocalDate date;

    }

}
