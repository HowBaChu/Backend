package com.HowBaChu.howbachu.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void init() {
        for (int i = 0; i < 10; i++) {
            memberRepository.save(Member.builder()
                .email("test@naver.com" + i)
                .password("testPassword" + i)
                .username("testUsername" + i)
                .mbti(MBTI.ENTJ)
                .statusMessage("testStatusMessage" + i)
                .build());
        }
    }

    @Test
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