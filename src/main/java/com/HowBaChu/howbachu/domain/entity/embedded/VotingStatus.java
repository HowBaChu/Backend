package com.HowBaChu.howbachu.domain.entity.embedded;


import com.HowBaChu.howbachu.domain.constants.Selection;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class VotingStatus {

    private int A;
    private int B;

    public VotingStatus() {
        A = 1;
        B = 1;
    }

    public void updateVotingStatus(Selection selection) {
        if (selection == Selection.A) A++;
        else B++;
    }
}

