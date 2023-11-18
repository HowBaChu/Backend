package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.entity.*;
import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.HowBaChu.howbachu.repository.LikesRepository;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.service.impl.LikesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class LikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OpinRepository opinRepository;

    @InjectMocks
    private LikesServiceImpl likesService;

    private Topic duumyTopic;
    private Member dummyMember;
    private Member dummyLikedMember;
    private Vote dummyVote;
    private Opin dummyOpin;

    @BeforeEach
    void init() {
        duumyTopic = Topic.builder()
            .id(65L)
            .title("탕수육은 부먹인가, 찍먹인가?")
            .subTitle(new SubTitle("부먹이다", "찍먹이다"))
            .date(LocalDate.now())
            .votingStatus(new VotingStatus())
            .build();

        dummyMember = Member.builder()
            .id(123L)
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

        dummyVote = Vote.builder()
            .id(1235L)
            .topic(duumyTopic)
            .member(dummyMember)
            .selection(Selection.A)
            .selectSubTitle(duumyTopic.getSubTitle().getSub_A())
            .build();

        dummyOpin = Opin.builder()
            .id(2153L)
            .vote(dummyVote)
            .parent(null)
            .content("아니.. 당연히 부먹이지.. 미친 거 아닌가")
            .isDeleted(false)
            .likeCnt(10)
            .build();
    }


    @Test
    @DisplayName("좋아요 - 생성")
    public void addLikes() throws Exception {

        // given
        String email = dummyMember.getEmail();
        Long opinId = dummyOpin.getId();
        Likes dummyLikes = Likes.of(dummyMember, dummyOpin);

        when(memberRepository.findByEmail(any(String.class))).thenReturn(dummyMember);
        when(opinRepository.findById(any(Long.class))).thenReturn(Optional.of(dummyOpin));
        when(likesRepository.save(any(Likes.class))).thenReturn(dummyLikes);
        when(likesRepository.fetchLikesByEmailAndOpinId(any(String.class), any(Long.class)))
            .thenReturn(dummyLikes);

        // when
        Long likesId = likesService.addLikes(email, opinId);

        // then
        assertThat(likesId).isEqualTo(opinId);
        assertThat(dummyOpin.getLikeCnt()).isGreaterThan(10);
    }

    @Test
    @DisplayName("좋아요 - 취소")
    public void cancelLikes() throws Exception {
        // given
        String cancelerEmail = dummyLikedMember.getEmail();
        Long opinId = dummyOpin.getId();

        Likes dummyLikes = Likes.of(dummyLikedMember, dummyOpin);

        when(likesRepository.fetchLikesByEmailAndOpinId(any(String.class), any(Long.class)))
            .thenReturn(dummyLikes);
        doNothing().when(likesRepository).delete(dummyLikes);

        // when
        likesService.cancelLikes(cancelerEmail, opinId);

        // then
        verify(likesRepository).delete(dummyLikes);
        assertThat(dummyOpin.getLikeCnt()).isLessThan(10);
    }

}