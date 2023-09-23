package com.HowBaChu.howbachu.jwt;

import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 꺼내기
        String accessToken = resolveToken(request, "Access-Token");
        String refreshToken = resolveToken(request, "Refresh-Token");

        // 액세스 토큰과 리프레시 토큰이 모두 존재한다면
        if (accessToken != null && refreshToken != null) {
            // 리프레쉬 토큰이 유효하다면
            if (refreshToken.equals(
                refreshTokenRepository.findByKey(jwtProvider.getEmailFromToken(accessToken)).get()
                    .getValue())) {
                // 액세스 토큰이 유효하다면
                if (jwtProvider.validateToken(accessToken)) {
                    setAuthentication(jwtProvider.getEmailFromToken(accessToken), request);
                    chain.doFilter(request, response);
                }
                // 액세스 토큰이 유효하지 않다면
                else {
                    // 리프레쉬 토큰이 유효하다면
                    if (jwtProvider.validateToken(refreshToken)) {
                        // 액세스 토큰 재발급
                        String accessTokenNew = jwtProvider.createAccessToken(
                            jwtProvider.getEmailFromToken(refreshToken));
                        setAuthentication(jwtProvider.getEmailFromToken(accessTokenNew), request);
                    }
                    // 리프레쉬 토큰이 유효하지 않다면
                    else {
                        log.info("RefreshToken이 만료됨");
                        throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
                    }
                }
            }
        } else {
            chain.doFilter(request, response);
        }

            // 액세스 토큰이 유효하지 않다면 -> 리프레쉬 토큰과 액세스 토큰 모두 갱신
            // 리프레쉬 토큰이 유효하지 않다면 -> 탈취 확인
        // 둘 중 하나라도 없다면 로그인 필요


        /*if (accessToken == null) {
            log.info("AccessToken이 비어있음.");
            throw new CustomException(ErrorCode.NO_JWT_TOKEN);
        }

        if (refreshToken == null) {
            log.info("RefreshToken이 비어있음.");
            throw new CustomException(ErrorCode.NO_JWT_TOKEN);
        }

        Boolean accessTokenStatus = jwtProvider.validateToken(accessToken);
        String email = jwtProvider.getEmailFromToken(accessToken);
        Optional<RefreshToken> refreshTokenOld = refreshTokenRepository.findByKey(email);

        if (accessTokenStatus) {
            setAuthentication(email, request);
        } // 액세스 토큰이 유효하다면
        else if (refreshTokenOld.isPresent()) {
            if (!refreshToken.equals(refreshTokenOld)) {
                log.info("탈취된 토큰으로 접근 시도");
                throw new CustomException(ErrorCode.HACKED_TOKEN);
            }
            log.info("AccessToken이 만료됨");// 액세스 토큰이 만료되었으며 리프레쉬토큰이 존재하는 경우
            if (jwtProvider.validateToken(refreshTokenOld.get().getValue())) { // 액세스 토큰이 만려되면 리프레쉬 토큰으로 새로운 액세스 토큰 발급
                log.info("RefreshToken이 유효함");

                String accessTokenNew = jwtProvider.createAccessToken(email);
                jwtProvider.setHeaderAccessToken(response, accessTokenNew);

                String refreshTokenNew = jwtProvider.createRefreshToken();
                refreshTokenOld.get().updateValue(refreshTokenNew);
                jwtProvider.setHeaderRefreshToken(response, refreshTokenNew);

                setAuthentication(email, request);
            } else {
                log.info("RefreshToken이 만료됨");
                throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
            } // 액세스 토큰과 리프레쉬 토큰이 모두 만료되었다면 익셉션 발생 -> 재로그인 필요
        } else {
            log.info("RefreshToken이 존재하지 않음. 재로그인 필요.");
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        } // 액세스 토큰이 만료되고 리프레쉬 토큰이 존재하지 않는 경우
        chain.doFilter(request, response);*/
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


    // 요청에 담긴 헤더에서 토큰값 가져오기
    // 토큰의 타입에 따라 다르게 가져올 수 있음.
    private static String resolveToken(HttpServletRequest request, String tokenType) {
        String headerAuth = request.getHeader(tokenType);
        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }
        return null;
    }


}