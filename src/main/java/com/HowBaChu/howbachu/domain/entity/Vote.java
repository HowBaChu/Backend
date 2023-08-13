package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.constants.Option;
import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote {

    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Option option;


    public static Vote of(VoteRequestDto requestDto, Topic topic, Member member) {
        Vote vote = Vote.builder()
            .topic(topic)
            .member(member)
            .option(requestDto.getOption())
            .build();
        vote.voting(requestDto.getOption());
        return vote;
    }

    /**
     * 선택된 옵션을 저장하고, 토픽의 투표 상태를 업데이트.
     * @param selectedOption
     */
    public void voting(Option selectedOption) {
        this.option = selectedOption;
        topic.getVotingStatus().updateVotingStatus(selectedOption);
    }


}
