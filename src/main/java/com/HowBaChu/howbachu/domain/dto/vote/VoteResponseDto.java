package com.HowBaChu.howbachu.domain.dto.vote;

import com.HowBaChu.howbachu.domain.constants.Selection;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {
    private Selection status;
}
