package com.HowBaChu.howbachu.domain.dto.opin;

import com.HowBaChu.howbachu.domain.constants.Selection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinResponseDto {

    private Long id;
    private String topicSubTitle;
    private Selection selection;
    private String nickname;
    private String content;
    private int likeCnt;

}
