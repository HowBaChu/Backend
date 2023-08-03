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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE user_id = ?")
@Where(clause = "is_deleted != true")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    @Column
    private Boolean isDeleted;

    @Column
    private String statusMessage;

    public static Member toEntity(MemberRequestDto requestDto) {
        return Member.builder()
            .email(requestDto.getEmail())
            .password(requestDto.getPassword())
            .username(requestDto.getUsername())
            .mbti(requestDto.getMbti())
            .statusMessage(requestDto.getStatusMessage())
            .isDeleted(false)
            .build();
    }

    public void update(MemberRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
        this.mbti = requestDto.getMbti();
        this.statusMessage = requestDto.getStatusMessage();
    }

    public void delete() { this.isDeleted = true; }

}
