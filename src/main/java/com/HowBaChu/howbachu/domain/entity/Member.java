package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Column
    private String statusMessage;

    public static Member toEntity(MemberRequestDto requestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
            .email(requestDto.getEmail())
            .password(passwordEncoder.encode(requestDto.getPassword()))
            .username(requestDto.getUsername())
            .mbti(requestDto.getMbti())
            .statusMessage(requestDto.getStatusMessage())
            .build();
    }

    public void update(MemberRequestDto requestDto, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(requestDto.getPassword());
        this.username = requestDto.getUsername();
        this.mbti = requestDto.getMbti();
        this.statusMessage = requestDto.getStatusMessage();
    }
}
