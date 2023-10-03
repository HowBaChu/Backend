package com.HowBaChu.howbachu.domain.dto.opin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpinThreadResponseDto {
    OpinResponseDto parentOpin;
    List<OpinResponseDto> childOpinList;
}
