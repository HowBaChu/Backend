package com.HowBaChu.howbachu.domain.dto.report;

import com.HowBaChu.howbachu.domain.constants.ReportType;
import com.HowBaChu.howbachu.domain.entity.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDto {

    private ReportType type;
    private String content;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = Shape.STRING)
    private LocalDateTime date;

    public static ReportResponseDto of(Report report) {
        return ReportResponseDto.builder()
            .type(report.getType())
            .content(report.getReportedContent())
            .reason(report.getReason())
            .date(report.getCreatedAt())
            .build();
    }
}
