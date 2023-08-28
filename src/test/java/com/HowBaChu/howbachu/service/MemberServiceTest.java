package com.HowBaChu.howbachu.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.jwt.JwtProvider;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberServiceTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void init() {
        memberRequestDto = new MemberRequestDto("testEmail", "testPassword", "testUsername",
            MBTI.ENTJ, "testStatusMessage");
    }

    @Test
    @DisplayName("회원가입 테스트 - 서비스")
    void signup() {
        //given

        //when
        memberService.signup(memberRequestDto);

        //then
        Member savedMember = memberRepository.findByEmail(memberRequestDto.getEmail());
        assertThat(savedMember.getEmail()).isEqualTo(memberRequestDto.getEmail());
        assertThat(passwordEncoder.matches("testPassword", savedMember.getPassword())).isTrue();
        assertThat(savedMember.getUsername()).isEqualTo(memberRequestDto.getUsername());
        assertThat(savedMember.getMbti()).isEqualTo(memberRequestDto.getMbti());
        assertThat(savedMember.getStatusMessage()).isEqualTo(memberRequestDto.getStatusMessage());
    }

    @Test
    @DisplayName("로그인 테스트 - 서비스")
    void login() {
        //given
        memberService.signup(memberRequestDto);

        //when
        memberRequestDto.encodePassword("testPassword");
        TokenResponseDto tokenResponseDto = memberService.login(memberRequestDto);

        //then
        assertThat(jwtProvider.getEmailFromToken(tokenResponseDto.getAccessToken())).isEqualTo(
            memberRequestDto.getEmail());
    }

    @Test
    @DisplayName("회원정보 수정 테스트 - 서비스")
    void updateMember() {
        //given
        memberService.signup(memberRequestDto);
        memberRequestDto.encodePassword("testPassword");
        memberService.login(memberRequestDto);

        //when
        MemberRequestDto updateRequestDto = new MemberRequestDto("testEmail", "testPassword", "testUsername",
            MBTI.ENTJ, "updateStatusMessage");
        MemberResponseDto memberResponseDto = memberService.updateMember(memberRequestDto.getEmail(), updateRequestDto);

        //then
        assertThat(memberResponseDto.getStatusMessage()).isEqualTo(
            updateRequestDto.getStatusMessage());
    }

    @Test
    @DisplayName("회원 삭제 테스트 - 서비스")
    void deleteMember() {
        //given
        memberService.signup(memberRequestDto);
        memberRequestDto.encodePassword("testPassword");
        memberService.login(memberRequestDto);

        //when
        memberService.deleteMember(memberRequestDto.getEmail());

        //then
        assertThat(memberRepository.existsByEmail(memberRequestDto.getEmail())).isFalse();
        assertThat(refreshTokenRepository.findByKey(memberRequestDto.getEmail())).isEqualTo(
            Optional.empty());
    }

    @Test
    @DisplayName("회원 이메일 중복 확인 테스트 - 서비스")
    void checkEmailDuplicate() {
        //given
        memberService.signup(memberRequestDto);

        //when

        //then
        assertThat(memberService.checkEmailDuplicate(memberRequestDto.getEmail()).getStatus()).isTrue();
    }

    @Test
    @DisplayName("회원 유저네임 중복 확인 테스트 - 서비스 ")
    void checkUsernameDuplicate() {
        //given
        memberService.signup(memberRequestDto);

        //when

        //then
        assertThat(memberService.checkUsernameDuplicate(memberRequestDto.getUsername()).getStatus()).isTrue();
    }

    @Test
    @DisplayName("회원 로그아웃 테스트 - 서비스")
    void logout() {
        //given
        memberService.signup(memberRequestDto);
        memberRequestDto.encodePassword("testPassword");
        memberService.login(memberRequestDto);

        assertThat(refreshTokenRepository.findByKey(memberRequestDto.getEmail())).isNotEqualTo(Optional.empty());

        //when
        memberService.logout(memberRequestDto.getEmail());

        //then
        assertThat(refreshTokenRepository.findByKey(memberRequestDto.getEmail())).isEqualTo(
            Optional.empty());
    }

}