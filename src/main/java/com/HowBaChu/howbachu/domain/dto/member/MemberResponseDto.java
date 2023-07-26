package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String email;
    private String password;
    private String username;
    private MBTI mbti;
    private String statusMessage;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .password(member.getPassword())
            .username(member.getUsername())
            .mbti(member.getMbti())
            .statusMessage(member.getStatusMessage())
            .build();
    }

    @Override
    public String toString() {
        return "MemberResponseDto{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", username='" + username + '\'' +
            ", mbti='" + mbti + '\'' +
            ", statusMessage='" + statusMessage + '\'' +
            '}';
    }
}
