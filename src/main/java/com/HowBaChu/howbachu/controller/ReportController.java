package com.HowBaChu.howbachu.controller;

import com.HowBaChu.howbachu.domain.constants.ResponseCode;
import com.HowBaChu.howbachu.domain.dto.report.ReportRequestDto;
import com.HowBaChu.howbachu.domain.dto.response.ResResult;
import com.HowBaChu.howbachu.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/report")
public class ReportController {

    private final ReportService reportService;

    // 신고하기
    @PostMapping("")
    public ResponseEntity<ResResult> reportOpin(
        @ApiIgnore Authentication authentication,
        @RequestBody ReportRequestDto requestDto
    ) {
        reportService.createReport(requestDto, authentication.getName());
        return ResponseCode.REPORT_SAVE.toResponse(null);
    }

    // 신고당한 내역 확인
    @GetMapping("")
    public ResponseEntity<ResResult> findReport(@ApiIgnore Authentication authentication) {
        return ResponseCode.REPORT_FIND.toResponse(reportService.findReports(authentication.getName()));
    }

}
