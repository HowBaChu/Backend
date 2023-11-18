package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponseDto {

    private Boolean status;

    public ResponseCode passwordCheck() {
        return status ? ResponseCode.PASSWORD_CORRECT : ResponseCode.PASSWORD_DISCORD;
    }
}
