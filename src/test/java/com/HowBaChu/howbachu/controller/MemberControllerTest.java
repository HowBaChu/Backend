package com.HowBaChu.howbachu.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenResponseDto;
import com.HowBaChu.howbachu.domain.dto.member.MemberRequestDto;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.repository.MemberRepository;
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

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;

    MemberRequestDto memberRequestDto;
    TokenResponseDto tokenResponseDto;
    String content;

    @BeforeEach
    void init() {
        memberRequestDto = new MemberRequestDto("testEmail", "testPassword", "testUsername",
            MBTI.ENTJ, "testStatusMessage");

        memberService.signup(memberRequestDto);
        memberRequestDto.encodePassword("testPassword");
        tokenResponseDto = memberService.login(memberRequestDto);

    }

    @Test
    @DisplayName("회원 상세정보 조회 테스트 - 컨트롤러")
    void findMemberDetail() throws Exception {
        //given

        //when
        mockMvc.perform(
                get("/api/v1/member")
                    .header("Authorization", tokenResponseDto.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.email").exists())
            .andExpect(jsonPath("$.data.username").exists())
            .andExpect(jsonPath("$.data.mbti").exists())
            .andExpect(jsonPath("$.data.statusMessage").exists())
            .andDo(print());

        //then

    }

    @Test
    @DisplayName("회원 정보 수정 테스트 - 컨틀롤러")
    void updateMemberDetail() throws Exception {
        //given
        MemberRequestDto updateRequestDto = new MemberRequestDto("testEmail", "testPassword", "testUsername22",
            MBTI.ENTJ, "testStatusMessage");
        Gson gson = new Gson();
        content = gson.toJson(updateRequestDto);

        //when
        mockMvc.perform(
                patch("/api/v1/member")
                    .header("Authorization", tokenResponseDto.getAccessToken())
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.email").exists())
            .andExpect(jsonPath("$.data.username").exists())
            .andExpect(jsonPath("$.data.mbti").exists())
            .andExpect(jsonPath("$.data.statusMessage").exists())
            .andDo(print());

        //then
        assertThat(memberRepository.findByEmail(memberRequestDto.getEmail()).getUsername()).isEqualTo(
            updateRequestDto.getUsername());
    }

    @Test
    @DisplayName("회원 삭제 테스트 - 컨트롤러")
    void deleteMember() throws Exception {
        //given

        //when
        mockMvc.perform(
                delete("/api/v1/member")
                    .header("Authorization", tokenResponseDto.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("204"))
            .andDo(print());

        //then
        assertThrows(CustomException.class, () -> {
            memberRepository.findByEmail(memberRequestDto.getEmail());
        });
    }

    @Test
    @DisplayName("이메일로 중복 회원 검사 테스트 - 컨트롤러")
    void checkEmailDuplicate() throws Exception {
        //given

        //when
        mockMvc.perform(
                get("/api/v1/member/email/{email}/exists",memberRequestDto.getEmail())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("true"))
            .andDo(print());

        //then
    }

    @Test
    @DisplayName("유저네임으로 중복 회원 검사 테스트 - 컨트롤러")
    void checkUsernameDuplicate() throws Exception {
        //given

        //when
        mockMvc.perform(
                get("/api/v1/member/username/{username}/exists",memberRequestDto.getUsername())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.status").value("true"))
            .andDo(print());
        //then
    }
}