package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.base.BaseEntity;
import com.HowBaChu.howbachu.domain.constants.ReportType;
import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import com.HowBaChu.howbachu.domain.entity.embedded.ReportPK;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @EmbeddedId
    private ReportPK reportPK;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id")
    @MapsId("reporterId")
    private Member reporter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reported_id")
    @MapsId("reportedId")
    private Member reported;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false)
    private String content;

    @Column
    private String reason;

    public static Report toEntity(ReportRequestDto requestDto, Member reporter, Member reported, String opinContent) {
        return Report.builder()
            .reportPK(new ReportPK(reporter.getId(), reported.getId()))
            .reporter(reporter)
            .reported(reported)
            .type(requestDto.getType())
            .reason(
                ! requestDto.getType().equals(ReportType.ETC)
                ? requestDto.getType().getReason()
                : requestDto.getReason())
            .content(opinContent)
            .build();
    }
}
