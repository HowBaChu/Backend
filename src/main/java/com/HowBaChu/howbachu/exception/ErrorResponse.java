package com.HowBaChu.howbachu.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String message;
    private HttpStatus httpStatus;

    public ErrorResponse(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }
}
