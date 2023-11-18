package com.HowBaChu.howbachu.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto.login;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto.signup;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto.update;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

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
    @Autowired
    HttpServletResponse response;

    private MemberRequestDto.signup memberSignupDto;
    private MemberRequestDto.login memberLoginDto;
    private MemberRequestDto.update memberUpdateDto;


    @BeforeEach
    void init() {
        memberSignupDto = new signup("testEmail@naver.com", "testPassword!123", "testUsername",
            MBTI.ENTJ);
        memberLoginDto = new login("testEmail@naver.com", "testPassword!123");
    }

    @Test
    @DisplayName("회원가입 테스트 - 서비스")
    void signup() {
        //given

        //when
        memberService.signup(memberSignupDto);

        //then
        Member savedMember = memberRepository.findByEmail(memberSignupDto.getEmail());
        assertThat(savedMember.getEmail()).isEqualTo(memberSignupDto.getEmail());
        assertThat(passwordEncoder.matches("testPassword!123", savedMember.getPassword())).isTrue();
        assertThat(savedMember.getUsername()).isEqualTo(memberSignupDto.getUsername());
        assertThat(savedMember.getMbti()).isEqualTo(memberSignupDto.getMbti());
//        assertThat(savedMember.getStatusMessage()).isEqualTo(memberSignupDto.getStatusMessage());
    }

    @Test
    @DisplayName("로그인 테스트 - 서비스")
    void login() {
        //given
        memberService.signup(memberSignupDto);

        //when
        memberSignupDto.encodePassword("testPassword!123");
        TokenResponseDto tokenResponseDto = memberService.login(memberLoginDto, response);

        //then
        assertThat(jwtProvider.getEmailFromToken(tokenResponseDto.getAccessToken())).isEqualTo(
            memberSignupDto.getEmail());
    }

    @Test
    @DisplayName("회원정보 수정 테스트 - 서비스")
    void updateMember() {
        //given
        memberService.signup(memberSignupDto);
        memberSignupDto.encodePassword("testPassword!123");
        memberService.login(memberLoginDto, response);

        //when
        MemberRequestDto.update updateRequestDto = new update("testEmail@naver.com", "testPassword!123",
            MBTI.ENTJ, "testStatusMessage");
        MemberResponseDto memberResponseDto = memberService.updateMember(memberSignupDto.getEmail(), updateRequestDto);

        //then
        assertThat(memberResponseDto.getStatusMessage()).isEqualTo(
            updateRequestDto.getStatusMessage());
    }

    @Test
    @DisplayName("회원 삭제 테스트 - 서비스")
    void deleteMember() {
        //given
        memberService.signup(memberSignupDto);
        memberSignupDto.encodePassword("testPassword!123");
        memberService.login(memberLoginDto, response);

        //when
        memberService.deleteMember(memberSignupDto.getEmail());

        //then
        assertThat(memberRepository.existsByEmail(memberSignupDto.getEmail())).isFalse();
//        assertThat(refreshTokenRepository.findByKey(memberSignupDto.getEmail())).isEqualTo(
//            Optional.empty());
    }

    @Test
    @DisplayName("회원 이메일 중복 확인 테스트 - 서비스")
    void checkEmailDuplicate() {
        //given
        memberService.signup(memberSignupDto);

        //when

        //then
        assertThat(memberService.checkEmailDuplicate(memberSignupDto.getEmail()).getStatus()).isTrue();
    }

    @Test
    @DisplayName("회원 유저네임 중복 확인 테스트 - 서비스 ")
    void checkUsernameDuplicate() {
        //given
        memberService.signup(memberSignupDto);

        //when

        //then
        assertThat(memberService.checkUsernameDuplicate(memberSignupDto.getUsername()).getStatus()).isTrue();
    }

    @Test
    @DisplayName("회원 로그아웃 테스트 - 서비스")
    void logout() {
        //given
        memberService.signup(memberSignupDto);
        memberSignupDto.encodePassword("testPassword!123");
        memberService.login(memberLoginDto, response);

//        assertThat(refreshTokenRepository.findByKey(memberSignupDto.getEmail())).isNotEqualTo(Optional.empty());

        //when
        memberService.logout(memberSignupDto.getEmail(), response);

        //then
//        assertThat(refreshTokenRepository.findByKey(memberSignupDto.getEmail())).isEqualTo(
//            Optional.empty());
    }

}