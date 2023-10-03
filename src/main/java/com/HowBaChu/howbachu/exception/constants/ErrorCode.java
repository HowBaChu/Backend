package com.HowBaChu.howbachu.exception.constants;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    /* COMMON */
    NOT_AUTHORIZED_CONTENT(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),

    /* AUTH */
    NO_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 정보가 존재하지 않습니다. 다시 로그인해 주세요."),
    NOT_AUTHORIZED_TOKEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "로그인 정보가 유효하지 않습니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "로그인 정보 형식이 올바르지 않습니다."),
    INVALID_TOKEN_STRUCTURE(HttpStatus.UNAUTHORIZED, "로그인 정보가 올바르지 않습니다."),
    MODIFIED_TOKEN_DETECTED(HttpStatus.UNAUTHORIZED, "로그인 정보가 변경되었습니다."),
    EMAIL_VERIFICATION_FAILED(HttpStatus.FORBIDDEN, "인증에 실패하였습니다."),

    /* MEMBER */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 회원입니다."),
    WRONG_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    FILE_NOT_EXIST(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),
    SEIZED_TOKEN_DETECTED(HttpStatus.FORBIDDEN, "토큰 정보가 잘못되었습니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "이미 가입된 이메일 입니다."),
    USERNAME_DUPLICATION(HttpStatus.BAD_REQUEST,"이미 존재하는 닉네임 입니다."),

    /* VOTE */
    VOTE_ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 투표에 참가하셨습니다."),
    VOTE_NOT_FOUND(HttpStatus.NOT_FOUND, "투표 정보가 존재하지 않습니다."),

    /* OPIN */
    OPIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 오핀입니다."),
    OPIN_MISS_MATCH(HttpStatus.BAD_REQUEST, "해당 오핀과 정보가 일치하는 유저가 없습니다."),

    /* TOPIC */
    TOPIC_NOT_FOUND(HttpStatus.NOT_FOUND, "토픽 정보가 존재하지 않습니다."),

    /* LIKES */
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요 정보가 존재하지 않습니다."),
    LIKES_ALREADY_EXISTS(HttpStatus.NOT_FOUND, "이미 좋아요 정보가 있습니다."),

    /* REPORT */
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고한 사용자 입니다."),;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
