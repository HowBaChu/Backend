package com.HowBaChu.howbachu.domain.dto.opin;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinThreadResponseDto {
    OpinResponseDto parentOpin;
    List<OpinResponseDto> childOpinList;
}
