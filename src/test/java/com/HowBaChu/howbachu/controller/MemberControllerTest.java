package com.HowBaChu.howbachu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenDto;
import com.HowBaChu.howbachu.validation.LimitBound;
import com.google.gson.Gson;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Gson gson;

    MemberSingupTestDto memberSignupDto;
    MemberLoginTestDto memberLoginDto;
    TokenDto tokenDto;

    String testEmail = "testEmail@howbachu.com";
    String testPassword = "testPassword!123";
    String testUsername = "testUsername";
    String testMBTI = MBTI.INFP.name();
    String testStatusMessage = "testStatusMessage";

    final String ACCESS_TOKEN = "Access-Token";
    final String REFRESH_TOKEN = "Refresh-Token";

    @BeforeEach
    void init() throws Exception {
        tokenDto = new TokenDto();

        // 회원가입
        memberSignupDto = new MemberSingupTestDto(testEmail, testPassword,
            testUsername, testMBTI);
        mockMvc.perform(post("/api/v1/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(memberSignupDto)));

        // 로그인
        memberLoginDto = new MemberLoginTestDto(testEmail, testPassword);
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(memberLoginDto)))
            .andReturn().getResponse();

        // 토큰 설정
        tokenDto.setAccessToken(response.getCookie(ACCESS_TOKEN).getValue());
        tokenDto.setRefreshToken(response.getCookie(REFRESH_TOKEN).getValue());
    }

    @Nested
    @DisplayName("회원 상세정보 조회 테스트")
    class testFindMemberDetail {

        @Test
        @DisplayName("회원 상세 정보를 조회할 수 있다.")
        void find_Member_Detail_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(get("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("회원정보 수정 테스트")
    class testUpdateMemberDetail {

        @Test
        @DisplayName("유저네임을 수정할 수 있다.")
        void update_Member_Username_Success() throws Exception {
            //given
            String targetUsername = "targetName";

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(targetUsername, testMBTI,
                testPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("MBTI를 수정할 수 있다.")
        void update_Member_MBTI_Success() throws Exception {
            //given
            String targetMBTI = MBTI.ENFJ.name();

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, targetMBTI,
                testPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("비밀번호를 수정할 수 있다.")
        void update_Member_Password_Success() throws Exception {
            //given
            String targetPassword = "targetPassword!123";

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, testMBTI,
                targetPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("상태메세지를 수정할 수 있다.")
        void update_Member_StatusMessage_Success() throws Exception {
            //given
            String targetStatusMessage = "targetStatusMessage";

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, testMBTI,
                testPassword, targetStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }

        @ParameterizedTest
        @CsvSource(value = {"thisStringOver12Letters", "w"})
        @DisplayName("유저네임은" + LimitBound.userNameMinLength + "자 이상" + LimitBound.userNameMaxLength
            + "자 이하만 입력할 수 있다.")
        void update_Member_With_Wrong_Username(String wrongUsername) throws Exception {
            //given

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(wrongUsername, testMBTI,
                testPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isBadRequest()).andDo(print());
        }

        @ParameterizedTest
        @CsvSource(value = {"thisStringOver30LetterForHowBaChuStatusMessageTestCode"})
        @DisplayName("상태메세지는" + LimitBound.statusMessageMaxLength + "자 이하만 입력할 수 있다.")
        void update_Member_With_Wrong_StatusMessage_Range(String wrongStatusMessage)
            throws Exception {
            //given

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, testMBTI,
                testPassword, wrongStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @CsvSource(value = {"thisStringOver24LettersForHowbachu", "wwdsdad"})
        @DisplayName("비밀번호는" + LimitBound.passwordMinLength + "자 이상" + LimitBound.passwordMaxLength
            + "자 이하만 입력할 수 있다.")
        void update_Member_With_Wrong_Password_Range(String wrongPassword) throws Exception {
            //given

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, testMBTI,
                wrongPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @CsvSource(value = {"thisStri321", "wwdsaddsadad", "1111111111"})
        @DisplayName("비밀번호는 특수문자와 영어, 숫자를 포함해야 한다.")
        void update_Member_With_Wrong_Password_Type(String wrongPassword)
            throws Exception {
            //given

            //when
            MemberUpdateTestDto memberUpdateDto = new MemberUpdateTestDto(testUsername, testMBTI,
                wrongPassword, testStatusMessage);

            //then
            mockMvc.perform(patch("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberUpdateDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("회원탈퇴 테스트")
    class testDeleteMember {

        @Test
        @DisplayName("회원을 탈퇴할 수 있다.")
        void delete_Member_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(delete("/api/v1/member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("이메일 중복 검사 테스트")
    class testCheckEmailDuplicate {

        @Test
        @DisplayName("이메일 중복을 검사할 수 있다.")
        void check_Email_Duplicate_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(get("/api/v1/member/exists/email/{email}", testEmail)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("가입되지 않은 이메일도 중복을 검사할 수 있다.")
        void check_Email_Duplicate_With_Wrong_Email() throws Exception {
            //given
            String wrongEmail = "nonRegistered@test.com";

            //when

            //then
            mockMvc.perform(get("/api/v1/member/exists/email/{email}", wrongEmail)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("닉네임 중복 검사 테스트")
    class testCheckUsernameDuplicate {

        @Test
        @DisplayName("닉네임 중복을 검사할 수 있다.")
        void check_Username_Duplicate_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(get("/api/v1/member/exists/username/{username}", testUsername)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("가입되지 않은 닉네임도 중복을 검사할 수 있다.")
        void checkUsernameDuplicateFail() throws Exception {
            //given
            String wrongUsername = "nonRegistered";
            //when

            //then
            mockMvc.perform(get("/api/v1/member/exists/username/{username}", wrongUsername)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("비밀번호 확인 테스트")
    class testPasswordVerification {

        @Test
        @DisplayName("비밀번호를 검증할 수 있다.")
        void check_Password_Verification_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(post("/api/v1/member/password-verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberLoginDto))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("틀린 비밀번호는 400 에러를 반환한다.")
        void checkPasswordVerificationFail() throws Exception {
            //given
            String wrongPassword = "wrongPassword!123";
            //when

            //then
            mockMvc.perform(post("/api/v1/member/password-verification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(new MemberLoginTestDto(testEmail, wrongPassword)))
                    .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                    .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isBadRequest());
        }

    }

    private class MemberSingupTestDto {

        private String email;
        private String password;
        private String username;
        private String mbti;

        public MemberSingupTestDto(String testEmail, String testPassword, String testUsername,
            String testMBTI) {
            this.email = testEmail;
            this.password = testPassword;
            this.username = testUsername;
            this.mbti = testMBTI;
        }

        public void changeEmail(String email) {
            this.email = email;
        }

        public void changePassword(String password) {
            this.password = password;
        }

        public void changeUsername(String username) {
            this.username = username;
        }

        public void changeMBTI(String mbti) {
            this.mbti = mbti;
        }

    }

    private class MemberUpdateTestDto {

        private String password;
        private String username;
        private String mbti;
        private String statusMessage;

        public MemberUpdateTestDto(String username, String mbti, String password,
            String statusMessage) {
            this.username = username;
            this.mbti = mbti;
            this.password = password;
            this.statusMessage = statusMessage;
        }

        public void changeUsername(String username) {
            this.username = username;
        }

        public void changeMBTI(String mbti) {
            this.mbti = mbti;
        }

        public void changePassword(String password) {
            this.password = password;
        }

        public void changeStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }

    }

    private class MemberLoginTestDto {

        private String email;
        private String password;

        public MemberLoginTestDto(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public void changeEmail(String email) {
            this.email = email;
        }

        public void changePassword(String password) {
            this.password = password;
        }
    }

}
