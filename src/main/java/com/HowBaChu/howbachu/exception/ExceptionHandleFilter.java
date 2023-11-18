package com.HowBaChu.howbachu.exception;


import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class ExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            log.error("CustomException", e);
            ResponseEntity<String> errorResponse = handleCustomException(e);
            response.setStatus(errorResponse.getStatusCodeValue());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(errorResponse.getBody());
        }
    }

    private ResponseEntity<String> handleCustomException(CustomException e) {
        // 에러 메시지 출력
        String errorMessage = e.getErrorCode().getMessage();
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus httpStatus = e.getErrorCode().getStatus();

        // ResponseEntity를 사용하여 응답 생성
        return ResponseEntity.status(httpStatus)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .body("{\"errorCode\":\""+errorCode.name()+"\",\"code\":\""+errorCode.getCode()+"\",\"message\":\""+errorMessage+"\"}");
    }
}
