package com.HowBaChu.howbachu.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ExceptionHandleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            log.error("Filter Exception Caught");
            log.error("FilterCustomException: "+ e.getErrorCode().getMessage());
            response.setStatus(e.getErrorCode().getStatus().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new ObjectMapper().writeValueAsString(e.getErrorCode().toString());
            response.getWriter().write(json);
        }
    }
}
