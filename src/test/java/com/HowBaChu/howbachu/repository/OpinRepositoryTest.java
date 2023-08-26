package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.Topic;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;


@DataJpaTest
@ActiveProfiles(value = "test")
class OpinRepositoryTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    OpinRepository opinRepository;

    @PersistenceContext
    EntityManager em;

    private Vote vote;

    @BeforeEach
    public void init() {
        Topic topic = Topic.builder()
            .title("탕수육은 부먹인가, 찍먹인가")
            .date(LocalDate.now())
            .subTitle(new SubTitle("부먹", "찍먹"))
            .votingStatus(new VotingStatus())
            .build();
        em.persist(topic);

        Member member = Member.builder()
            .email("eamil@email.com")
            .password("123123123")
            .username("username")
            .mbti(MBTI.ENFJ)
            .statusMessage("statusMessage")
            .isDeleted(false)
            .build();
        em.persist(member);


        vote = Vote.builder()
            .topic(topic)
            .member(member)
            .selection(Selection.A)
            .build();
        em.persist(vote);
    }

    @Test
    @DisplayName(value = "Opin - 루트 생성 테스트")
    public void opinCreateTest() {
        // given
        Opin opin = Opin.of("나는 아무래도 찍먹이다,..", vote);

        // when
        Long savedId = opinRepository.save(opin).getId();
        Opin findOpin = opinRepository.findById(savedId).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
        );

        // then
        Assertions.assertThat(findOpin.getContent()).isEqualTo(opin.getContent());
    }
}