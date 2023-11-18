package com.HowBaChu.howbachu.repository;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.entity.*;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
class LikesRepositoryTest {

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    OpinRepository opinRepository;

    private Topic duumyTopic;
    private Member dummyMember;
    private Member dummyLikedMember;
    private Vote dummyVote;
    private Opin dummyOpin;

    @BeforeEach
    void init() {
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
        dummyLikedMember = Member.builder()
            .email("starbucks@good.coffee")
            .password("123123")
            .username("스타벅스굿커피")
            .mbti(MBTI.ENTJ)
            .isDeleted(false)
            .statusMessage("콜드브루 짱")
            .avatar(null)
            .build();
        memberRepository.save(dummyMember);
        memberRepository.save(dummyLikedMember);

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
    @DisplayName("fetchLikesByEmailAndOpinId() 테스트")
    public void fetchLikesByEmailAndOpinId() throws Exception {

        // given
        Likes savedLikes = likesRepository.save(Likes.of(dummyLikedMember, dummyOpin));

        // when
        Likes result = likesRepository.fetchLikesByEmailAndOpinId(dummyLikedMember.getEmail(), dummyOpin.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(savedLikes.getMember().getEmail()).isEqualTo(dummyLikedMember.getEmail());
        Assertions.assertThat(savedLikes.getOpin().getMember().getEmail()).isEqualTo(dummyMember.getEmail());
    }




}