package com.HowBaChu.howbachu.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import com.HowBaChu.howbachu.service.MemberService;
import com.google.gson.Gson;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    MemberRequestDto memberRequestDto;
    String content;

    @BeforeEach
    void init() {
        memberRequestDto = new MemberRequestDto("testEmail", "testPassword", "testUsername",
            MBTI.ENTJ, "testStatusMessage");
        Gson gson = new Gson();
        content = gson.toJson(memberRequestDto);
    }

    @Test
    void register() throws Exception {
        //given

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
        assertThat(memberRepository.existsByEmail(memberRequestDto.getEmail())).isTrue();
    }

    @Test
    void login() throws Exception {
        //given
        memberRequestDto.encodePassword("testPassword");
        memberService.signup(memberRequestDto);

        //when
        mockMvc.perform(
                post("/api/v1/auth/login")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andDo(print());

        //then
        assertThat(refreshTokenRepository.findByKey(memberRequestDto.getEmail())).isNotEqualTo(
            Optional.empty());
    }

    @Test
    void logout() throws Exception {
        //given
        memberService.signup(memberRequestDto);
        memberRequestDto.encodePassword("testPassword");
        TokenResponseDto tokenResponseDto = memberService.login(memberRequestDto);

        //when
        mockMvc.perform(
                post("/api/v1/auth/logout")
                    .header("Authorization", tokenResponseDto.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());

        //then
        assertThat(refreshTokenRepository.findByKey(memberRequestDto.getEmail())).isEqualTo(
            Optional.empty());
    }
}