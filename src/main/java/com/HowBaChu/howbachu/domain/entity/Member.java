package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.base.BaseEntity;
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
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
@Where(clause = "is_deleted != true")
public class Member extends BaseEntity {

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

    @Column
    private String avatar;

    @Column
    private int reportCnt = 0;

    public static Member toEntity(MemberRequestDto.signup requestDto) {
        return Member.builder()
            .email(requestDto.getEmail())
            .password(requestDto.getPassword())
            .username(requestDto.getUsername())
            .mbti(MBTI.findByCode(requestDto.getMbti()))
            .isDeleted(false)
            .build();
    }

    public void update(MemberRequestDto.update requestDto) {
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
        this.mbti = MBTI.findByCode(requestDto.getMbti());
        this.statusMessage = requestDto.getStatusMessage();
    }

    public void uploadAvatar(String url) {
        this.avatar = url;
    }

    public void addReportCnt() {
        this.reportCnt++;
    }

    public boolean isReported(String email) {
        return this.email.equals(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member )) return false;
        return id != null && id.equals(((Member) o).getId());
    }

}
