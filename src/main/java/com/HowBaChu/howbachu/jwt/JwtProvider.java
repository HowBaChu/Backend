package com.HowBaChu.howbachu.jwt;

import com.HowBaChu.howbachu.config.JwtConfig;
import com.HowBaChu.howbachu.domain.dto.jwt.TokenDto;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtConfig jwtConfig;

    /**
     * @param email 유저의 이메일
     * @return accessToken 과 refreshToken 이 담긴 token 객체를 리턴한다.
     */
    public TokenDto generateJwtToken(String email) {
        return TokenDto.builder()
            .accessToken(createAccessToken(email))
            .refreshToken(createRefreshToken())
            .key(email)
            .build();
    }

    /**
     * @param email 유저 이메일
     * @return 새로운 accessToken 발급
     */
    public String createAccessToken(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getAccessExpirationTime()))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()))
            .compact();
    }

    /**
     * @return 새로운 refreshToken 발급
     */
    public String createRefreshToken() {
        return Jwts.builder()
            .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpirationTime()))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()))
            .compact();
    }

    /**
     * @param token Claims 부분을 가져올 token
     * @return token 의 정보가 담긴 claims
     * @implNote 토큰을 해독하여 토큰에 담긴 정보를 가져온다.
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()))
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * @param token 만료검증할 토큰
     * @return 만료되지 않았다면 1, 만료되었다면 0, 그 외의 예외는 익센셥 처리
     */
    public Boolean validateToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (SignatureException e) {
            throw new CustomException(ErrorCode.MODIFIED_TOKEN_DETECTED);
        }
        return true;
    }

    public int getAccessTokenExpiredTime() {
        return jwtConfig.getAccessExpirationTime();
    }

    public int getRefreshTokenExpiredTime() {
        return jwtConfig.getRefreshExpirationTime();
    }
}
