package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.base.BaseEntity;
import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.dto.vote.VoteRequestDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {

    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private Selection selection;

    private String selectSubTitle;

    public static Vote of(VoteRequestDto requestDto, Topic topic, Member member) {
        Vote vote = Vote.builder()
            .topic(topic)
            .member(member)
            .selection(requestDto.getSelection())
            .build();
        vote.voting(requestDto.getSelection());
        vote.setSelectSubTitle(requestDto.getSelection());
        return vote;
    }

    /**
     * 선택된 옵션을 저장하고, 토픽의 투표 상태를 업데이트.
     * @param selectedSelection
     */
    public void voting(Selection selectedSelection) {
        this.selection = selectedSelection;
        topic.getVotingStatus().updateVotingStatus(selectedSelection);
    }

    private void setSelectSubTitle(Selection selection) {
        this.selectSubTitle = selection == Selection.A ? topic.getSubTitle().getSub_A() : topic.getSubTitle().getSub_B();
    }

}
