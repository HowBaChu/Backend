package com.HowBaChu.howbachu.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        for (int i = 0; i < 10; i++) {
            memberRepository.save(Member.builder()
                .email("test@naver.com" + i)
                .password("testPassword!123" + i)
                .username("testUsername" + i)
                .mbti(MBTI.ENTJ)
                .statusMessage("testStatusMessage" + i)
                .isDeleted(false)
                .build());
        }
    }

    @Test
    @DisplayName("회원 상세 조회 테스트 - 레포지토리")
    void findByEmail() {
        //given
        String testEmail = "test@naver.com";

        //when

        //then
        for (int i = 0; i < 10; i++) {
            assertThat(memberRepository.findByEmail(testEmail+i).getEmail()).isEqualTo(testEmail+i);
        }
    }

    @Test
    @DisplayName("이메일로 회원 찾기 테스트(Boolean) - 레포지토리")
    void existsByEmail() {
        //given
        String testEmail = "test@naver.com";

        //when

        //then
        for (int i = 0; i < 10; i++) {
            assertThat(memberRepository.existsByEmail(testEmail+i)).isTrue();
        }
    }

    @Test
    @DisplayName("유저네임으로 회원 찾기 테스트(Boolean) - 레포지토리")
    void existsByUsername() {
        //given
        String testUsername = "testUsername";

        //when

        //then
        for (int i = 0; i < 10; i++) {
            assertThat(memberRepository.existsByUsername(testUsername + i)).isTrue();
        }
    }

}