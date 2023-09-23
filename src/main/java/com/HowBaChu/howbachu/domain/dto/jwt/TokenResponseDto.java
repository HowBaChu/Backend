package com.HowBaChu.howbachu.domain.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
}
