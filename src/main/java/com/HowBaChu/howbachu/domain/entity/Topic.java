package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.entity.embedded.TopicSubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Topic {

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private LocalDate date;

    @Embedded
    private TopicSubTitle topicSubTitle;

    @Embedded
    private VotingStatus votingStatus;

}
