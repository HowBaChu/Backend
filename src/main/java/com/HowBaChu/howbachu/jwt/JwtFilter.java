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

    private final String ACCESS_TOKEN_HEADER = "Access-Token";
    private final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        if (isLoginRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        handleTokens(
            resolveTokenFromCookie(request, ACCESS_TOKEN_HEADER),
            resolveTokenFromCookie(request, REFRESH_TOKEN_HEADER),
            request, response);

        chain.doFilter(request, response);

    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/v1/auth/login")
            || request.getRequestURI().equals("/api/v1/auth/signup");
    }

    private void handleTokens(String accessToken, String refreshToken, HttpServletRequest request,
        HttpServletResponse response) {
        if (accessToken != null && refreshToken != null) {
            handleBothTokenExists(accessToken, refreshToken, request, response);
            return;
        }
        if (accessToken == null && refreshToken != null) {
            handleRefreshTokenOnly(refreshToken, request, response);
            return;
        }
        if (accessToken != null) {
            handleAccessTokenOnly(accessToken, request, response);
            return;
        }
        handleNoTokens();
    }

    private void handleBothTokenExists(String accessToken, String refreshToken,
        HttpServletRequest request, HttpServletResponse response) {

        String email = jwtProvider.getEmailFromToken(accessToken);
        RefreshToken refreshTokenOld = fetchRefreshTokenByEmail(email);

        refreshTokenOld.validateRefreshTokenRotate(refreshToken);

        jwtProvider.validateToken(accessToken);
        jwtProvider.validateToken(refreshToken);

        reissue(email, refreshTokenOld, response);
        setAuthentication(email, request);
    }

    private RefreshToken fetchRefreshTokenByEmail(String email) {
        return refreshTokenRepository.findByValue(email)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    private RefreshToken fetchRefreshTokenById(String refreshToken) {
        return refreshTokenRepository.findByValue(refreshToken)
            .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    private void handleRefreshTokenOnly(String refreshToken, HttpServletRequest request,
        HttpServletResponse response) {
        jwtProvider.validateToken(refreshToken);

        RefreshToken refreshTokenOld = fetchRefreshTokenById(refreshToken);
        String email = refreshTokenOld.getValue();

        reissue(email, refreshTokenOld, response);
        setAuthentication(email, request);
    }

    private void handleAccessTokenOnly(String accessToken, HttpServletRequest request,
        HttpServletResponse response) {
        jwtProvider.validateToken(accessToken);

        String email = jwtProvider.getEmailFromToken(accessToken);
        RefreshToken refreshTokenOld = fetchRefreshTokenByEmail(email);

        reissue(email, refreshTokenOld, response);
        setAuthentication(email, request);

    }

    private void handleNoTokens() {
        throw new CustomException(ErrorCode.NOT_AUTHORIZED_TOKEN);
    }

    private void reissue(String email, RefreshToken refreshTokenOld, HttpServletResponse response) {
        TokenDto tokenDto = jwtProvider.generateJwtToken(email);
        setAccessTokenCookie(response, tokenDto.getAccessToken());
        setRefreshTokenCookie(response, tokenDto.getRefreshToken());
        saveRefreshTokenCache(email, refreshTokenOld, tokenDto);
    }

    private void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        cookieUtil.setCookie(response, ACCESS_TOKEN_HEADER, accessToken,
            jwtProvider.getAccessTokenExpiredTime());
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        cookieUtil.setCookie(response, REFRESH_TOKEN_HEADER, refreshToken,
            jwtProvider.getRefreshTokenExpiredTime());
    }

    private void saveRefreshTokenCache(String email, RefreshToken refreshTokenOld,
        TokenDto tokenDto) {
        refreshTokenRepository.deleteById(refreshTokenOld.getKey());
        refreshTokenRepository.save(
            new RefreshToken(tokenDto.getRefreshToken(), email,
                jwtProvider.getRefreshTokenExpiredTime()));
    }

    private void setAuthentication(String email, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(
                email, "", List.of(new SimpleGrantedAuthority("USER")));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private String resolveTokenFromCookie(HttpServletRequest request, String tokenType) {
        for (Cookie cookie : request.getCookies()) {
            if (!tokenType.equals(cookie.getName())) {
                continue;
            }
            return cookie.getValue();
        }
        return null;
    }


}