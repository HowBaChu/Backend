package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import com.HowBaChu.howbachu.domain.dto.report.ReportResponseDto;
import com.HowBaChu.howbachu.domain.entity.Member;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.Report;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.repository.ReportRepository;
import com.HowBaChu.howbachu.service.ReportService;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final OpinRepository opinRepository;

    @Override
    public void createReport(ReportRequestDto requestDto, String reporterEmail) {
        Member reporter = memberRepository.findByEmail(reporterEmail);
        Opin reportedOpin = validateAlreadyReported(
            findOpin(requestDto.getReportedOpinId()), reporter);
        reportedOpin.addReport();
        reportRepository.save(Report.toEntity(requestDto, reporter, reportedOpin));
    }

    @Override
    public List<ReportResponseDto> findReports(String memberEmail) {
        return reportRepository.findAll().stream()
            .filter(report -> report.getReporter().getEmail().equals(memberEmail))
            .map(ReportResponseDto::of).collect(Collectors.toList());
    }

    private Opin findOpin(Long opinId) {
        return opinRepository.findById(opinId)
            .orElseThrow(() -> new CustomException(ErrorCode.OPIN_NOT_FOUND));
    }

    private Opin validateAlreadyReported(Opin opin, Member reporter) {
        if (reportRepository.findByReporterAndReported(reporter, opin).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED);
        }
        if (reporter.equals(opin.getMember())) {
            throw new CustomException(ErrorCode.CANNOT_REPORT_OWN_OPIN);
        }
        return opin;
    }

}
