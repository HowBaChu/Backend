package com.HowBaChu.howbachu.exception;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            handleCustomException(response, e);
        }
    }

    private void handleCustomException(HttpServletResponse response, CustomException e) {

        // 에러 메시지 출력
        log.error("Filter Exception Caught");
        log.error("CustomException: " + e.getErrorCode().getMessage());

        // HTTP Response 의 상태 코드와 컨텐츠 타입 설정
        response.setStatus(e.getErrorCode().getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 에러 메시지를 JSON 형태로 변환 후 응답 본문 추가
            response.getWriter().write(new ObjectMapper().writeValueAsString(e.getErrorCode().toString()));
        } catch (IOException exception) {
            // JSON 변환 중 에러 발생 시, 에러 메시지 출력
            log.error("Error while processing response json", exception);
        }
    }
}
