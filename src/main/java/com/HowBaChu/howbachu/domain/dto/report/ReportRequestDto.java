package com.HowBaChu.howbachu.domain.dto.report;

import com.HowBaChu.howbachu.domain.constants.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReportRequestDto {

    private Long reportedOpinId;
    @Schema(example = "ETC")
    private ReportType type;

    @Schema(example = "testReason")
    private String reason;
}
