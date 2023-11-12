package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.base.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE opin SET is_deleted = true WHERE opin_id = ? OR report_cnt >= ReportCriteria.OPIN_SUSPENSION_COUNT.getCount()")
@Where(clause = "is_deleted != true")
public class Opin extends BaseEntity {

    @Id
    @Column(name = "opin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Opin parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Opin> children = new ArrayList<>();

    @Column(nullable = false, length = 300)
    private String content;

    private int likeCnt;

    @Column
    private Boolean isDeleted;

    @Column
    private int reportCnt = 0;

    /**
     * Root Opin
     * @param content
     * @param vote
     */
    public static Opin of(String content, Vote vote) {
        return Opin.builder()
            .content(content)
            .vote(vote)
            .isDeleted(false)
            .build();
    }

    /**
     * Reply Opin
     * @param content
     * @param vote
     * @param parentOpin
     */
    public static Opin of(String content, Vote vote, Opin parentOpin) {
        Opin childOpin = Opin.builder()
            .content(content)
            .vote(vote)
            .parent(parentOpin)
            .isDeleted(false)
            .build();
        parentOpin.addChildOpin(childOpin);
        return childOpin;
    }

    private void addChildOpin(Opin childOpin) {
        this.children.add(childOpin);
        childOpin.setParentOpin(this);
    }

    private void setParentOpin(Opin parentOpin) {
        this.parent = parentOpin;
    }

    public void updateContent(String content) {
        this.content = content;
    }


    public void addLikes() {
        this.likeCnt++;
    }

    public void cancelLikes() {
        if (likeCnt == 0) return;
        this.likeCnt--;
    }

    public void addReport() {
        this.reportCnt++;
        addReportedMemberCnt();
    }

    public Member getMember() {
        return this.vote.getMember();
    }

    public void addReportedMemberCnt() {
        this.getMember().addReportCnt();
    }
}
