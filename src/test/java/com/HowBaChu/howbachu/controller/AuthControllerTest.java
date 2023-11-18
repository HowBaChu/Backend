package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto.login;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import com.HowBaChu.howbachu.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    HttpServletResponse response;

    MemberRequestDto.signup memberSignupDto;
    MemberRequestDto.login memberLoginDto;
    String content;
    Gson gson = new Gson();

    @BeforeEach
    void init() {
        memberSignupDto = new MemberRequestDto.signup("testEmail@naver.com", "testPassword!123", "testUsername",
            MBTI.ENTJ);
        memberLoginDto = new login("testEmail@naver.com", "testPassword!123");

    }

    @Test
    @DisplayName("회원가입 테스트 - 컨트롤러")
    void register() throws Exception {
        //given
        content = gson.toJson(memberSignupDto);

        //when
        mockMvc.perform(
                post("/api/v1/auth/signup")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.responseCode").exists())
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists())
            .andDo(print());

        //then
        assertThat(memberRepository.existsByEmail(memberSignupDto.getEmail())).isTrue();
    }

    @Test
    @DisplayName("로그인 테스트 - 컨트롤러")
    void login() throws Exception {
        //given
        memberSignupDto.encodePassword("testPassword!123");
        memberService.signup(memberSignupDto);
        content = gson.toJson(memberLoginDto);
        //when
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andDo(print());

        //then
//        assertThat(refreshTokenRepository.findByKey(memberSignupDto.getEmail())).isNotEqualTo(
//            Optional.empty());
    }

    @Test
    @DisplayName("로그아웃 테스트 - 컨트롤러")
    void logout() throws Exception {
        //given
        memberService.signup(memberSignupDto);
        memberSignupDto.encodePassword("testPassword!123");
        TokenResponseDto tokenResponseDto = memberService.login(memberLoginDto, response);

        //when
        mockMvc.perform(
                post("/api/v1/auth/logout")
                    .header("Authorization", tokenResponseDto.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        //then
//        assertThat(refreshTokenRepository.findByKey(memberSignupDto.getEmail())).isEqualTo(
//            Optional.empty());
    }
}