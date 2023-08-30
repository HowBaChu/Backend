package com.HowBaChu.howbachu.domain.constants;

import lombok.Getter;

@Getter
public enum ReportType {
    SPAM("스팸 내용"),
    AVERSION("욕설 및 혐오 발언"),
    INAPPROPRIATE_CONTENT("부적절한 내용"),
    INAPPROPRIATE_PROFILE("부적절한 프로필"),
    PRIVACY_LEAK("개인정보 유출"),
    FALSEHOOD("허위정보"),
    COPYRIGHT("저작권 침해"),
    ETC("기타");

    private final String reason;
    ReportType(String reason) {
        this.reason = reason;
    }
}
