package com.HowBaChu.howbachu.exception.constants;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    /* JWT */
    NO_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"토큰이 존재하지 않습니다."),
    NOT_AUTHORIZED_TOKEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "재로그인이 필요합니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "올바른 토큰 형식이 아닙니다."),
    INVALID_TOKEN_STRUCTURE(HttpStatus.UNAUTHORIZED, "올바른 토큰 구조가 아닙니다."),
    MODIFIED_TOKEN_DETECTED(HttpStatus.UNAUTHORIZED, "토큰 변조가 감지되었습니다."),

    /* MEMBER */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "가입되지 않은 회원입니다"),
    WRONG_LOGIN_REQUEST(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
