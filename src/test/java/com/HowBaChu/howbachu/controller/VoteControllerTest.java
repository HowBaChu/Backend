package com.HowBaChu.howbachu.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import com.HowBaChu.howbachu.jwt.JwtProvider;
import com.HowBaChu.howbachu.service.MemberService;
import com.HowBaChu.howbachu.service.VoteService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletResponse;


@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(VoteController.class)
class VoteControllerTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VoteService voteService;

    @MockBean
    Authentication authentication;

    @MockBean
    MemberService memberService;

    @MockBean
    HttpServletResponse response;

    private String token;

    @MockBean
    private JwtProvider jwtTokenProvider;

    @BeforeEach
    void init() {
        token = "Bearer fake.jwt.token.here";
//        given(jwtTokenProvider.validateToken(anyString()))
        given(jwtTokenProvider.getEmailFromToken(anyString())).willReturn("testUser");
    }

    @Test
    @DisplayName("[POST] 투표 - 성공")
    public void votingTest() throws Exception {

        // given
        VoteRequestDto requestDto = new VoteRequestDto(Selection.A);
        given(voteService.voting(requestDto, authentication.getName()))
            .willReturn(1L);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/vote")
            .header("Authorization", token)
            .content(new Gson().toJson(requestDto))
            .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated())
            .andExpect(jsonPath("$.responseCode").exists())
            .andExpect(jsonPath("$.code").exists())
            .andExpect(jsonPath("$.message").exists())
            .andExpect(jsonPath("$.data").exists())
            .andDo(print());

        verify(voteService).voting(requestDto, authentication.getName());
    }

}