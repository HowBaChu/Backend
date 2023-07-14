package com.HowBaChu.howbachu.domain.dto.response;


import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResResult<T> {

    private ResponseCode responseCode;
    private String code;
    private String message;
    private T data;

}
