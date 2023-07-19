package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import lombok.Getter;

@Getter
public class MemberRequestDto {

    private Long id;
    private String email;
    private String password;
    private String username;
    private MBTI mbti;
}
