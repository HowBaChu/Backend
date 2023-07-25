package com.HowBaChu.howbachu.exception;

import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "errorCode=" + errorCode +
                '}';
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

}
