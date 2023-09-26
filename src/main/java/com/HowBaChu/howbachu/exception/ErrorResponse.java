package com.HowBaChu.howbachu.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
