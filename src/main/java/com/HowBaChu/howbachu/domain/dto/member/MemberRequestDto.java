package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberRequestDto {

    private Long id;
    private String email;
    private String password;
    private String username;
    private MBTI mbti;
    private String statusMessage;

    public void encodePassword(String encoded) { this.password = encoded; }
}
