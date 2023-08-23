package com.HowBaChu.howbachu.domain.dto.vote;

import com.HowBaChu.howbachu.domain.constants.Selection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {
    private Selection selection;
}
