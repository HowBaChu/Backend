package com.HowBaChu.howbachu.domain.dto.member;

import lombok.Getter;

@Getter
public class MemberRequestDto {

    private Long id;
    private String email;
    private String password;
    private String username;
}
