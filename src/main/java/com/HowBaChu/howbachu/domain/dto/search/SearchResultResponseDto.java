package com.HowBaChu.howbachu.domain.dto.search;

import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponseDto {

    Page<ParentsOpinSearchResponseDto> parentsOpinList;
    Page<ChildOpinSearchResponseDto> childOpinList;
    Page<TopicSearchResponseDto> topicList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* P-Opin 검색 결과 */
    public static class ParentsOpinSearchResponseDto {

        private Long opinId;

        /* 댓글 작성자 프로필 이미지 */
        private String profileImg;

        /* 댓글 작성자 */
        private String nickname;

        /* 댓글 내용 */
        private String content;

        /* 좋아요 갯수 */
        private int likeCnt;

        /* 투표한 부제 */
        private String topicSubtitle;

        /* 좋아요 여부 */
        private boolean isLiked;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* Ch-Opin 검색 결과 */
    public static class ChildOpinSearchResponseDto {

        private Long opinId;

        /* 댓글 작성자 프로필 이미지 */
        private String profileImg;

        /* 댓글 작성자 */
        private String nickname;

        /* 댓글 내용 */
        private String content;

        /* 좋아요 갯수 */
        private int likeCnt;

        /* 투표한 부제 */
        private String topicSubtitle;

        /* 부모 댓글 Id */
        private Long parentOpinId;

        /* 좋아요 여부 */
        private boolean isLiked;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor  /* Topic 검색 결과 */
    public static class TopicSearchResponseDto {

        private Long topicId;

        /* 토픽 제목 */
        private String title;

        /* 토픽 부제목 */
        private SubTitle subTitle;

        /* 투표 현황 */
        private VotingStatus votingStatus;

        /* 토픽 날짜(MM:dd) */
        private LocalDate date;

    }

}
