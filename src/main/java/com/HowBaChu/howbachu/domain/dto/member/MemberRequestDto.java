package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String email;
    private String password;
    private String username;
    private MBTI mbti;
    private String statusMessage;

    public void encodePassword(String encoded) { this.password = encoded; }
}
