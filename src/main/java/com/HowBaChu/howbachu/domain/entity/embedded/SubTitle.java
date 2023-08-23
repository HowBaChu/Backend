package com.HowBaChu.howbachu.domain.entity.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class SubTitle {

    private String sub_A;
    private String sub_B;

}
