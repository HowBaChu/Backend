package com.HowBaChu.howbachu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    Gson gson;

    MemberRequestTestDto memberSignupDto;
    MemberLoginTestDto memberLoginDto;

    String testEmail = "testEmail@howbachu.com";
    String testPassword = "testPassword!123";
    String testUsername = "testUsername";
    String testMBTI = MBTI.INFP.name();

    final String ACCESS_TOKEN = "Access-Token";
    final String REFRESH_TOKEN = "Refresh-Token";

    @BeforeEach
    void init() {
        memberSignupDto = new MemberRequestTestDto(testEmail, testPassword,
            testUsername, testMBTI);
        memberLoginDto = new MemberLoginTestDto(testEmail, testPassword);
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class testRegister {

        @Test
        @DisplayName("정상적인 회원가입이 가능하다.")
        void register_With_Normal_Columns() throws Exception {
            //given

            // when

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        }

        @Test
        @DisplayName("이메일은 이메일 형식에 맞춰 입력해야 한다.")
        void register_With_Bad_Email() throws Exception {
            //given
            String badEmail = testEmail.replace("@", "+");

            // when
            memberSignupDto.changeEmail(badEmail);

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        }

        @ParameterizedTest
        @CsvSource(value = {"1234567891011121314asbvb!!!!", "1a!"}, delimiter = ',')
        @DisplayName("비밀번호는" + LimitBound.passwordMinLength + "자 이상 "
            + LimitBound.passwordMaxLength + "자 이하로 입력해야 한다.")
        void register_With_Bad_Password(String badPassword) throws Exception {
            //given

            // when
            memberSignupDto.changePassword(badPassword);

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        }

        @ParameterizedTest
        @CsvSource(value = {"12345678", "abcdefgh", "!@#$%^&*"}, delimiter = ',')
        @DisplayName("비밀번호는 영문, 숫자, 특수문자를 포함해야 한다.")
        void register_With_Bad_Password2(String badPassword) throws Exception {
            //given

            // when
            memberSignupDto.changePassword(badPassword);

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        }

        @ParameterizedTest
        @CsvSource(value = {"1", "1234567891011121314aabb"}, delimiter = ',')
        @DisplayName("닉네임은" + LimitBound.userNameMinLength + "자 이상 "
            + LimitBound.userNameMaxLength + "자 이하로 입력해야 한다.")
        void register_With_Bad_Username(String badUsername) throws Exception {
            //given

            // when
            memberSignupDto.changeUsername(badUsername);

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        }

        @ParameterizedTest
        @CsvSource(value = {"ESSP", "EFGH", "SPFJ", "ESJF"}, delimiter = ',')
        @DisplayName("MBTI는 정확한 MBTI로 입력해야 한다.")
        void register_With_Bad_MBTI(String badMBTI) throws Exception {
            //given

            // when
            memberSignupDto.changeMBTI(badMBTI);

            // then
            mockMvc.perform(
                    post("/api/v1/auth/signup")
                        .content(convertToJson(memberSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        }

    }

    @Nested
    @DisplayName("로그인 테스트")
    class loginTest {

        @BeforeEach
        void init() throws Exception {
            mockMvc.perform(
                post("/api/v1/auth/signup")
                    .content(convertToJson(memberSignupDto))
                    .contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("틀린 비밀번호로는 로그인할 수 없다.")
        void login_With_Wrong_Password() throws Exception {
            //given
            String wrongPassword = testPassword.replace("!", "@");

            //when
            memberLoginDto.changePassword(wrongPassword);

            //then
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .content(convertToJson(memberLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("가입되지 않는 이메일로는 로그인할 수 없다.")
        void login_With_Not_Exist_Email() throws Exception {
            //given
            String notExistEmail = testEmail.replace("test", "wrongTest");

            //when
            memberLoginDto.changeEmail(notExistEmail);

            //then
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .content(convertToJson(memberLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("정상적으로 로그인이 가능하다.")
        void login_Success() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(
                    post("/api/v1/auth/login")
                        .content(convertToJson(memberLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("정상적으로 로그아웃이 가능하다.")
        void logout_Success() throws Exception {
            //given
            TokenDto tokenDto = new TokenDto();

            //when
            MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberLoginDto)))
                .andReturn().getResponse();

            tokenDto.setAccessToken(response.getCookie(ACCESS_TOKEN).getValue());
            tokenDto.setRefreshToken(response.getCookie(REFRESH_TOKEN).getValue());
            //then
            mockMvc.perform(
                    post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                        .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isNoContent());
        }

    }

    @Nested
    @DisplayName("이메일 인증 테스트")
    class mailVerificationTest {

        TokenDto tokenDto;

        @BeforeEach
        void init() throws Exception {
            tokenDto = new TokenDto();

            mockMvc.perform(
                post("/api/v1/auth/signup")
                    .content(convertToJson(memberSignupDto))
                    .contentType(MediaType.APPLICATION_JSON));

            MockHttpServletResponse response = mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(memberLoginDto)))
                .andReturn().getResponse();

            tokenDto.setAccessToken(response.getCookie(ACCESS_TOKEN).getValue());
            tokenDto.setRefreshToken(response.getCookie(REFRESH_TOKEN).getValue());
        }

        @Test
        @DisplayName("이메일 형식에 맞지 않으면 인증 메일을 보낼 수 없다")
        void verification_Mail_Send_With_Bad_Email() throws Exception {
            //given
            String badEmail = testEmail.replace("@", "+");

            //when

            //then
            mockMvc.perform(
                    post("/api/v1/auth/mail-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                        .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken()))
                        .param("email", badEmail))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("정상적으로 인증 메일을 보낼 수 있다")
        void verification_Mail_Send() throws Exception {
            //given

            //when

            //then
            mockMvc.perform(
                    post("/api/v1/auth/mail-verification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("email", testEmail)
                        .cookie(new Cookie(ACCESS_TOKEN, tokenDto.getAccessToken()))
                        .cookie(new Cookie(REFRESH_TOKEN, tokenDto.getRefreshToken())))
                .andExpect(status().isOk());
        }
    }

    private String convertToJson(Object content) {
        return gson.toJson(content);
    }

    private class MemberRequestTestDto {

        private String email;
        private String password;
        private String username;
        private String mbti;

        public MemberRequestTestDto(String testEmail, String testPassword, String testUsername,
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