package com.HowBaChu.howbachu.domain.entity.embedded;


import com.HowBaChu.howbachu.domain.constants.Selection;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class VotingStatus {

    private int A = 0;
    private int B = 0;

    public void updateVotingStatus(Selection selection) {
        if (selection == Selection.A) A++;
        else B++;
    }
}

