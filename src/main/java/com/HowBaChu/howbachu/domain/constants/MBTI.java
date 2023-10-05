package com.HowBaChu.howbachu.domain.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum MBTI {
    ISTJ, ISFJ, INFJ, INTJ, ISTP, ISFP, INFP, INTP,
    ESTP, ESFP, ENFP, ENTP, ESTJ, ESFJ, ENFJ, ENTJ;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MBTI findByCode(String code) {
        return Stream.of(MBTI.values())
            .filter(c -> c.name().equals(code))
            .findFirst()
            .orElse(null);
    }
}
