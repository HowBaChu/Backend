package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import com.HowBaChu.howbachu.domain.dto.report.ReportResponseDto;
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
    public void createReport(ReportRequestDto requestDto ,String reporterEmail) {
        Opin opin = opinRepository.findById(requestDto.getReportedOpinId())
            .orElseThrow(() -> new CustomException(ErrorCode.OPIN_NOT_FOUND));
        reportRepository.save(Report.toEntity(
                requestDto,
                memberRepository.findByEmail(reporterEmail),
                opin.getVote().getMember(),
                opin.getContent()
        ));
    }

    @Override
    public List<ReportResponseDto> findReports(String memberEmail) {
        return reportRepository.findByReported(memberRepository.findByEmail(memberEmail))
            .stream().map(ReportResponseDto::of).collect(Collectors.toList());
    }

}
