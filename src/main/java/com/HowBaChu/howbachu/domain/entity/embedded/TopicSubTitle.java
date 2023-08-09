package com.HowBaChu.howbachu.domain.entity.embedded;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TopicSubTitle {

    private String subTitle_1;
    private String subTitle_2;

}
