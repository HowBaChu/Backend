package com.HowBaChu.howbachu.domain.dto.opin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinRequestDto {
    private String content;
    private Long parentId;
}
