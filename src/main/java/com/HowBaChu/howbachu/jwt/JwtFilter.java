package com.HowBaChu.howbachu.jwt;

import com.HowBaChu.howbachu.domain.dto.jwt.TokenDto;
import com.HowBaChu.howbachu.domain.entity.RefreshToken;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import com.HowBaChu.howbachu.utils.CookieUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 꺼내기
        String accessToken = resolveTokenFromCookie(request, "Access-Token");
        String refreshToken = resolveTokenFromCookie(request, "Refresh-Token");
        log.info(accessToken+" // " +refreshToken);
        // 액세스 토큰과 리프레시 토큰이 모두 존재한다면
        if (accessToken != null && refreshToken != null) {
            String email = jwtProvider.getEmailFromToken(accessToken);
            RefreshToken refreshTokenOld = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
            // RTR을 통과한다면
            if (refreshToken.equals(refreshTokenOld.getValue())) {
                // 액세스 토큰이 유효하다면
                if (jwtProvider.validateToken(accessToken)) {
                    setAuthentication(email, request);
                    chain.doFilter(request, response);
                }
                // 액세스 토큰이 유효하지 않다면
                else {
                    // 리프레쉬 토큰이 유효하다면
                    if (jwtProvider.validateToken(refreshToken)) {
                        // 액세스 토큰 재발급
                        TokenDto tokenDto = jwtProvider.generateJwtToken(jwtProvider.getEmailFromToken(refreshToken));
                        cookieUtil.setCookie(response, "Access-Token",tokenDto.getAccessToken(), jwtProvider.getAccessTokenExpiredTime());
                        cookieUtil.setCookie(response, "Refresh-Token",tokenDto.getRefreshToken(), jwtProvider.getRefreshTokenExpiredTime());
                        refreshTokenOld.updateValue(tokenDto.getRefreshToken());
                        setAuthentication(email, request);
                    }
                    // 리프레쉬 토큰이 유효하지 않다면
                    else {
                        log.info("RefreshToken이 만료됨");
                        throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
                    }
                }
            }
            else { // 리프레쉬 토큰이 탈취되었다면
                log.info("인풋: "+refreshToken+" 기존:"+refreshTokenOld.getValue());
                throw new CustomException(ErrorCode.SEIZED_TOKEN_DETECTED);
            }
        } else { // 액세스 토큰과 리프레쉬 토큰이 모두 존재하지 않는다면
            chain.doFilter(request, response);
        }
    }

    // USER 권한 authentication 에 저장
    public void setAuthentication(String email, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                email,
                "",
                List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String resolveTokenFromCookie(HttpServletRequest request, String tokenType) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenType.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}