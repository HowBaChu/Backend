package com.HowBaChu.howbachu.domain.constants;

import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

@Getter
@ToString
public enum ResponseCode {

    /* OPIN */
    OPIN_LIST(HttpStatus.OK, "200", "댓글 조회 성공"),
    OPIN_CHILD_LIST(HttpStatus.OK, "200", "대댓글 조회 성공"),
    OPIN_SAVE(HttpStatus.CREATED, "201", "댓글 등록 성공"),
    OPIN_UPDATE(HttpStatus.OK, "200", "댓글 수정 성공"),
    OPIN_DELETE(HttpStatus.OK, "200", "댓글 삭제 성공"),

    /*AUTH*/
    MEMBER_SAVE(HttpStatus.CREATED, "201", "회원가입 성공"),
    MEMBER_LOGIN(HttpStatus.OK, "200", "로그인 성공"),
    MEMBER_LOGOUT(HttpStatus.OK, "200", "로그아웃 성공"),
    VERIFICATION_SEND(HttpStatus.OK, "200", "인증메일을 전송하였습니다."),
    VERIFICATION_SUCCESS(HttpStatus.OK, "200", "인증에 성공했습니다."),
    VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "401", "인증에 실패했습니다."),

    /* MEMBER */
    MEMBER_DETAIL(HttpStatus.OK, "200", "회원정보 불러오기 성공"),
    MEMBER_UPDATE(HttpStatus.OK, "200", "회원정보 수정 성공"),
    MEMBER_DELETE(HttpStatus.OK, "200", "회원정보 삭제 성공"),
    MEMBER_EXISTS(HttpStatus.OK, "200", "회원존재 여부 조회 성공"),
    AVATAR_UPLOAD(HttpStatus.OK, "200", "이미지 업로드 성공"),
    AVATAR_DELETE(HttpStatus.OK, "200", "이미지 삭제 성공"),
    PASSWORD_CORRECT(HttpStatus.OK, "200", "기존 비밀번호가 일치합니다."),
    PASSWORD_DISCORD(HttpStatus.BAD_REQUEST, "400", "기존 비밀번호가 틀렸습니다."),

    /* VOTE */
    VOTING_SUCCESS(HttpStatus.CREATED, "201", "투표 성공"),
    VOTING_UPDATE(HttpStatus.OK, "200", "투표 수정 성공"),
    VOTING_DELETE(HttpStatus.OK, "200", "투표 취소 성공"),

    /* REPORT */
    REPORT_SAVE(HttpStatus.CREATED, "201", "신고 성공"),
    REPORT_FIND(HttpStatus.OK, "200", "신고목록 조회 성공"),

    /* TOPIC */
    TOPIC_SUCCESS(HttpStatus.OK, "200", "토픽 조회 성공"),
    HONOR_TOPICS(HttpStatus.OK, "200", "명예의 전당 조회 성공"),

    /* LIKES */
    LIKES_ADD(HttpStatus.CREATED, "201", "좋아요 추가 성공"),
    LIKES_CANCEL(HttpStatus.OK, "200", "좋아요 취소 성공"),

    /* SEARCH */
    SEARCH_SUCCESS(HttpStatus.OK, "200", "검색 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ResponseCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public <T> ResponseEntity<Object> toResponse(@Nullable T data) {
        return new ResponseEntity<>(ResResult.builder()
            .responseCode(this)
            .code(this.getCode())
            .message(this.getMessage())
            .data(data)
            .build(), this.getHttpStatus());
    }

}
