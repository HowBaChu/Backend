package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.entity.*;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Transactional
@ActiveProfiles(value = "test")
class OpinRepositoryTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    OpinRepository opinRepository;

    @Autowired
    LikesRepository likesRepository;


    private Topic duumyTopic;
    private Member dummyMember;
    private Vote dummyVote;
    private Opin dummyOpin;

    @BeforeEach
    public void init() {
        duumyTopic = Topic.builder()
            .title("탕수육은 부먹인가, 찍먹인가?")
            .subTitle(new SubTitle("부먹이다", "찍먹이다"))
            .date(LocalDate.now())
            .votingStatus(new VotingStatus())
            .build();
        topicRepository.save(duumyTopic);

        dummyMember = Member.builder()
            .email("how@ba.chu")
            .password("123123")
            .username("하우바츄")
            .mbti(MBTI.ENFJ)
            .isDeleted(false)
            .statusMessage("취업시켜줘...")
            .avatar(null)
            .build();
        memberRepository.save(dummyMember);

        dummyVote = Vote.builder()
            .topic(duumyTopic)
            .member(dummyMember)
            .selection(Selection.A)
            .selectSubTitle(duumyTopic.getSubTitle().getSub_A())
            .build();
        voteRepository.save(dummyVote);

        dummyOpin = Opin.builder()
            .vote(dummyVote)
            .parent(null)
            .content("아니.. 당연히 부먹이지.. 미친 거 아닌가")
            .isDeleted(false)
            .build();
        opinRepository.save(dummyOpin);
    }

    @Test
    @DisplayName("Opin - 삭제를 위한, 조회 by opinID & email")
    public void fetchOpinByIdAndEmail_test() throws Exception {
        // given
        Long opinId = dummyOpin.getId();
        String email = dummyMember.getEmail();

        // when
        Opin findOpin = opinRepository.fetchOpin(opinId, email);

        // then
        assertThat(findOpin.getContent()).isEqualTo(dummyOpin.getContent());
        assertThat(findOpin.getVote().getSelection()).isEqualTo(dummyVote.getSelection());
    }

    @Test
    @DisplayName("Opin - 내가 쓴글 보기")
    public void fetchMyOpinList_test() throws Exception {
        // given
        String email = dummyMember.getEmail();
        for (int i = 1; i < 10; i++) {
            saveDummyOpin(i + "번째 Opin");
        }

        // when
        Page<OpinResponseDto> results = opinRepository.fetchMyOpinList(PageRequest.of(20, 0), email);

        // then
        assertThat(results.getContent().size()).isEqualTo(10);
        assertThat(results.getContent().get(0).getContent()).isEqualTo("9번째 Opin");

        for (OpinResponseDto responseDto : results.getContent()) {
            System.out.println(responseDto.getContent());
        }
    }

    private void saveDummyOpin(String content) {
        opinRepository.save(Opin.builder()
            .vote(dummyVote)
            .parent(null)
            .content(content)
            .isDeleted(false)
            .build());
    }
}