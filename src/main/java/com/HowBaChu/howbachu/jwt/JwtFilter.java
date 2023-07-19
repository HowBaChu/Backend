package com.HowBaChu.howbachu.jwt;

import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    final String SECRET_KEY;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain)
        throws IOException, ServletException {

        // 토큰 꺼내기
        String accessToken = resolveToken(request, "access");

        // 엑세스 토큰이 없다면 익셉션 발생 -> 에러 처리
        if (accessToken == null) {
            chain.doFilter(request, response);
            return;
        }

        String email = jwtProvider.getEmailFromToken(accessToken, SECRET_KEY);
        String refreshToken = refreshTokenRepository.findByKey(email);

        int accessToken_status = validateToken(accessToken, SECRET_KEY);
        if (accessToken_status == 1) { setAuthentication(email, request); } // 액세스 토큰이 유효하다면
        else if (refreshToken != null) { // 액세스 토큰이 만료되었으며 리프레쉬토큰이 존재하는 경우
            int refreshToken_status = validateToken(refreshToken, SECRET_KEY);
            if (refreshToken_status == 1) { // 액세스 토큰이 만려되면 리프레쉬 토큰으로 새로운 액세스 토큰 발급
                String newAccessToken = jwtProvider.createAccessToken(email, SECRET_KEY);
                jwtProvider.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(jwtProvider.getEmailFromToken(newAccessToken, SECRET_KEY),
                    request);
            } else { // 액세스 토큰과 리프레쉬 토큰이 모두 만료되었다면 익셉션 발생 -> 재로그인 필요
                response.setStatus(401);
                response.setHeader("code", "Refresh_Token_expired");
            }
        }
        else { // 액세스 토큰이 만료되고 리프레쉬 토큰이 존재하지 않는 경우
            response.setStatus(403);
            response.setHeader("code", "Mistaken_Access_Token");
        }
        chain.doFilter(request, response);
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
    private String resolveToken(HttpServletRequest request, String tokenType) {
        String headerAuth = tokenType.equals("access")
            ? request.getHeader("access")
            : request.getHeader("refresh");
        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }
        return null;
    }


    /**
     * @param token      만료검증할 토큰
     * @param SECRET_KEY 토큰 생성시 암호화에 사용된 키 값
     * @return 만료되지 않았다면 1, 만료되었다면 0, 그 외의 예외는 -1
     */
    public int validateToken(String token, String SECRET_KEY) {
        try {
            jwtProvider.parseClaims(token, SECRET_KEY);
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
            return 0;
        } catch (UnsupportedJwtException e) {
            log.error("올바른 JWT 토큰 형식이 아닙니다.");
            return -1;
        } catch (MalformedJwtException e) {
            log.error("올바른 JWT 토큰 구조가 아닙니다.");
            return -1;
        } catch (SignatureException e) {
            log.error("JWT 토큰이 변조되었습니다.");
            return -1;
        }
        return 1;
    }

}