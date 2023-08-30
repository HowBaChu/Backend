package com.HowBaChu.howbachu.service;

import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import com.HowBaChu.howbachu.domain.dto.report.ReportResponseDto;
import java.util.List;

public interface ReportService {

    // 신고 생성
    void createReport(ReportRequestDto requestDto, String reporterEmail);

    // 내가 받은 신고내역 조회
    List<ReportResponseDto> findReports(String memberEmail);
}
