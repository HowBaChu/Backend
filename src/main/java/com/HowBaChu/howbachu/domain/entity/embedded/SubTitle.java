package com.HowBaChu.howbachu.domain.entity.embedded;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SubTitle {

    private String sub_A;
    private String sub_B;

}
