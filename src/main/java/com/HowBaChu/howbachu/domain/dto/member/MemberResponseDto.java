package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponseDto {
    private Long id;
    private String email;
    private String password;
    private String username;

    public static MemberResponseDto of(Member member) {
        return MemberResponseDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .password(member.getPassword())
            .username(member.getUsername())
            .build();
    }

    @Override
    public String toString() {
        return "MemberResponseDto{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", username='" + username + '\'' +
            '}';
    }
}
