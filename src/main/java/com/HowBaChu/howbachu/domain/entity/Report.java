package com.HowBaChu.howbachu.domain.entity;

import com.HowBaChu.howbachu.domain.base.BaseEntity;
import com.HowBaChu.howbachu.domain.constants.ReportType;
import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    private Member reporter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "opin_id")
    private Opin reported;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column
    private String reason;

    public static Report toEntity(ReportRequestDto requestDto, Member reporter, Opin opin) {
        return Report.builder()
            .reporter(reporter)
            .reported(opin)
            .type(requestDto.getType())
            .reason(
                !requestDto.getType().equals(ReportType.ETC)
                    ? requestDto.getType().getReason()
                    : requestDto.getReason())
            .build();
    }

    public String getReportedContent() {
        return reported.getContent();
    }
}
