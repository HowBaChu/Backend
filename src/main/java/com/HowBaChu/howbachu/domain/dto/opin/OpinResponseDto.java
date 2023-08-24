package com.HowBaChu.howbachu.domain.dto.opin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinResponseDto {

    private Long id;
    private String topicSubTitle;
    private String nickname;
    private String content;
    private int likeCnt;

}