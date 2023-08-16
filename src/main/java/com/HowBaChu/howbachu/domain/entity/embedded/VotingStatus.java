package com.HowBaChu.howbachu.domain.entity.embedded;


import com.HowBaChu.howbachu.domain.constants.Option;
import lombok.Getter;

import javax.persistence.Embeddable;

@Getter
@Embeddable
public class VotingStatus {

    private int A = 0;
    private int B = 0;

    public void updateVotingStatus(Option option) {
        if (option == Option.A) A++;
        else B++;
    }
}

