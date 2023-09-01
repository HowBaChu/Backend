package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.entity.embedded.SubTitle;
import com.HowBaChu.howbachu.domain.entity.embedded.VotingStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private SubTitle subTitle;

    @Embedded
    private VotingStatus votingStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE)
    private List<Vote> voteList = new ArrayList<>();

}
