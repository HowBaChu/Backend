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

    //TODO
    //FIXME
    // - 리프레쉬 토큰 저장 시 값과 이메일을 바꾸기
    // - 액세스 토큰이 없을 시 리프레쉬 토큰으로 레디스에서 사용자 이메일 가져오기
    // - 그 정보를 바탕으로 토큰 갱신하기

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info(request.getRequestURI());
        if (request.getRequestURI().equals("/api/v1/auth/login") || request.getRequestURI().equals("/api/v1/auth/signup")) {
            chain.doFilter(request, response);
            return;
        }

        // 토큰 꺼내기
        String accessToken = resolveTokenFromCookie(request, "Access-Token");
        String refreshToken = resolveTokenFromCookie(request, "Refresh-Token");
        log.info("AccessToken: "+accessToken+" RefreshToken: " +refreshToken);

        // 액세스 토큰과 리프레쉬 토큰이 모두 있다면 -> RTR 수행
        if (accessToken != null && refreshToken != null) {
            log.info("토큰이 모두 존재");
            String email = jwtProvider.getEmailFromToken(accessToken);
            RefreshToken refreshTokenOld = refreshTokenRepository.findByValue(email)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
            // RTR 검사
            if (!refreshToken.equals(refreshTokenOld.getKey())) {
                log.info("RTR 실패");
                throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            // 액세스 토큰 검사 (API를 요청하고 바로 토큰이 만료되는 경우가 있을 수 있으므로 수행)
            if (!jwtProvider.validateToken(accessToken)) {
                log.info("AccessToken이 만료됨");
                // 액세스 토큰이 유효하지 않다면
                // 리프레쉬 토큰 검사 (API를 요청하고 바로 토큰이 만료 되는 경우가 있을 수 있으므로 수행)
                if (jwtProvider.validateToken(refreshToken)) {
                    log.info("RefreshToken이 유효함");
                    // 리프레쉬 토큰이 유효하다면
                    // 토큰 재발급
                    reissue(email, refreshTokenOld, response);
                    setAuthentication(email, request);
                }
                // 리프레쉬 토큰이 유효하지 않다면
                else {
                    log.info("RefreshToken이 만료됨");
                    throw new CustomException(ErrorCode.EXPIRED_REFESH_TOKEN);
                }
            }
            else {
                log.info("AccessToken이 유효함");
                setAuthentication(email, request);
            }
        }

        // 액세스 토큰이 없고 리프레쉬 토큰만 있다면 -> RTR 부터 수행
        else if (accessToken == null && refreshToken != null) {
            // 리프레퀴 토큰 검사
            RefreshToken refreshTokenOld = refreshTokenRepository.findById(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
            String email = refreshTokenOld.getValue();
            // 액세스 토큰이 유효하지 않다면
            // 리프레쉬 토큰 검사 (API를 요청하고 바로 토큰이 만료되는 경우가 있을 수 있으므로 수행)
            if (jwtProvider.validateToken(refreshToken)) {
                // 리프레쉬 토큰이 유효하다면
                // 토큰 재발급
                reissue(email, refreshTokenOld, response);
                setAuthentication(email, request);
            }
            // 리프레쉬 토큰이 유효하지 않다면
            else {
                log.info("RefreshToken이 만료됨");
                throw new CustomException(ErrorCode.EXPIRED_REFESH_TOKEN);
            }
        }
        // 리프레쉬 토큰이 없고 액세스 토큰만 있다면
        else if (accessToken != null && refreshToken == null) {
            if (!jwtProvider.validateToken(accessToken)) {
                throw new CustomException(ErrorCode.EXPIRED_REFESH_TOKEN);
            }
            else {
                String email = jwtProvider.getEmailFromToken(accessToken);
                RefreshToken refreshTokenOld = refreshTokenRepository.findByValue(email).orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
                reissue(email, refreshTokenOld, response);
                setAuthentication(email, request);
            }
        }
        else {
            throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
        }
        chain.doFilter(request, response);

    }

    public void reissue(String email, RefreshToken refreshTokenOld, HttpServletResponse response) {
        TokenDto tokenDto = jwtProvider.generateJwtToken(email);
        cookieUtil.setCookie(response, "Access-Token",tokenDto.getAccessToken(), jwtProvider.getAccessTokenExpiredTime());
        cookieUtil.setCookie(response, "Refresh-Token",tokenDto.getRefreshToken(), jwtProvider.getRefreshTokenExpiredTime());

        refreshTokenRepository.deleteById(refreshTokenOld.getKey());
        refreshTokenRepository.save(new RefreshToken(tokenDto.getRefreshToken(), email, jwtProvider.getRefreshTokenExpiredTime()));
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