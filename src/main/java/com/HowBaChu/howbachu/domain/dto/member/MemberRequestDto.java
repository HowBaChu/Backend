package com.HowBaChu.howbachu.domain.dto.member;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.validation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MemberRequestDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class signup {
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 아닙니다.")
        @Size(min = 1, max = 30, message = "이메일은 (1~30)자 사이로 입력해 주세요.")
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(message = "비밀번호는 영어, 숫자, 특수 문자를 포함한 (8~24)자 이어야 합니다.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,24}$")
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;

        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "이름은 (2~20)자 이어야 합니다.")
        @Schema(description = "사용자 닉네임", example = "testUserName")
        private String username;

        @EnumValid(enumClass = MBTI.class)
        private MBTI mbti;

        @Size(message = "상태메세지는 30자까지 입력할 수 있습니다.")
        @Schema(description = "상태메세지", example = "testStatusMessage")
        private String statusMessage;

        public void encodePassword(String encoded) { this.password = encoded; }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class login {
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 아닙니다.")
        @Size(min = 1, max = 30, message = "이메일은 (1~30)자 사이로 입력해 주세요.")
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(message = "비밀번호는 영어, 숫자, 특수 문자를 포함한 (8~24)자 이어야 합니다.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,24}$")
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class update {
        @NotBlank(message = "이메일을 입력해 주세요.")
        @Email(message = "이메일 형식이 아닙니다.")
        @Size(min = 1, max = 30, message = "이메일은 (1~30)자 사이로 입력해 주세요.")
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(message = "비밀번호는 영어, 숫자, 특수 문자를 포함한 (8~24)자 이어야 합니다.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,24}$")
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;

        @NotBlank(message = "이름을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "이름은 (2~20)자 이어야 합니다.")
        @Schema(description = "사용자 닉네임", example = "testUserNameUpdated")
        private String username;

        @EnumValid(enumClass = MBTI.class)
        private MBTI mbti;

        @Size(message = "상태메세지는 30자까지 입력할 수 있습니다.")
        @Schema(description = "상태메세지", example = "testStatusMessageUpdated")
        private String statusMessage;

        public void encodePassword(String encoded) { this.password = encoded; }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class passwordVerification{
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Pattern(message = "비밀번호는 영어, 숫자, 특수 문자를 포함한 (8~24)자 이어야 합니다.", regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,24}$")
        @Schema(description = "비밀번호", example = "testPassword!123")
        private String password;
    }




}
