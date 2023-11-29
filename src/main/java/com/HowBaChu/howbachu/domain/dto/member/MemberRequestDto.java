package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.validation.Email.EmailValid;
import com.HowBaChu.howbachu.validation.MBTI.MBTIValid;
import com.HowBaChu.howbachu.validation.Password.PasswordValid;
import com.HowBaChu.howbachu.validation.StatusMessage.StatusMessageValid;
import com.HowBaChu.howbachu.validation.Username.UsernameValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MemberRequestDto {

    private MemberRequestDto() {}

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class signup {
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        @EmailValid
        private String email;

        @Schema(description = "비밀번호", example = "testPassword!123")
        @PasswordValid
        private String password;

        @Schema(description = "사용자 닉네임", example = "testUserName")
        @UsernameValid
        private String username;

        @MBTIValid(enumClass = MBTI.class)
        private String mbti;

        public void encodePassword(String encoded) {
            this.password = encoded;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class login {

        @EmailValid
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;

        @PasswordValid
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class update {
        @PasswordValid
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;

        @UsernameValid
        @Schema(description = "사용자 닉네임", example = "testUserNameUpdated")
        private String username;

        @MBTIValid(enumClass = MBTI.class)
        private String mbti;

        @StatusMessageValid
        @Schema(description = "상태메세지", example = "testStatusMessageUpdated")
        private String statusMessage;

        public void encodePassword(String encoded) {
            this.password = encoded;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class passwordVerification {

        @PasswordValid
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;
    }


}
