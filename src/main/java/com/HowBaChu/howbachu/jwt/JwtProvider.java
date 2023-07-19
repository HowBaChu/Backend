package com.HowBaChu.howbachu.jwt;


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
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private final long accessTokenValidTime = Duration.ofMinutes(30).toMillis();
    private final long refreshTokenValidTime = Duration.ofDays(14).toMillis();


    /**
     * @param email      유저의 이메일
     * @param SECRET_KEY 토큰 생성시 암호화에 사용된 키 값
     * @return accessToken 과 refreshToken 이 담긴 token 객체를 리턴한다.
     */
    public TokenDto generateJwtToken(String email, String SECRET_KEY) {

        String accessToken = createAccessToken(email, SECRET_KEY);
        String refreshToken = createRefreshToken(SECRET_KEY);

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .key(email)
            .build();
    }

    /**
     * @param email      유저 이메일
     * @param SECRET_KEY 토큰 생성시 암호화에 사용된 키 값
     * @return 새로운 accessToken 발급
     */
    public String createAccessToken(String email, String SECRET_KEY) {
        return Jwts.builder()
            .setSubject(email)
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidTime))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(SECRET_KEY.getBytes()))
            .compact();
    }


    /**
     * @param SECRET_KEY 토큰 생성시 암호화에 사용된 키 값
     * @return 새로운 refreshToken 발급
     */
    public String createRefreshToken(String SECRET_KEY) {
        return Jwts.builder()
            .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidTime))
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(SECRET_KEY.getBytes()))
            .compact();
    }


    /**
     * @param response    클라이언트에 전달할 response
     * @param accessToken 헤더에 저장할 액세스 토큰
     */
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("access", accessToken);
    }


    /**
     * 토큰을 해독하여 토큰에 담긴 정보를 가져온다.
     *
     * @param token Claims 부분을 가져올 token
     * @return token 의 정보가 담긴 claims
     */
    public Claims parseClaims(String token, String SECRET_KEY) {
        return Jwts.parser()
            .setSigningKey(Base64.getEncoder().encode(SECRET_KEY.getBytes()))
            .parseClaimsJws(token)
            .getBody();
    }

    public String getEmailFromToken(String token, String secretKey) {
        return parseClaims(token, secretKey).getSubject();
    }

    /**
     * @param token      만료검증할 토큰
     * @param SECRET_KEY 토큰 생성시 암호화에 사용된 키 값
     * @return 만료되지 않았다면 1, 만료되었다면 0, 그 외의 예외는 -1
     */
    public Boolean validateToken(String token, String SECRET_KEY) {
        try {
            parseClaims(token, SECRET_KEY);
        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다.");
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("올바른 JWT 토큰 형식이 아닙니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (MalformedJwtException e) {
            log.error("올바른 JWT 토큰 구조가 아닙니다.");
            throw new CustomException(ErrorCode.INVALID_TOKEN_TYPE);
        } catch (SignatureException e) {
            log.error("JWT 토큰이 변조되었습니다.");
            throw new CustomException(ErrorCode.MODIFIED_TOKEN_DETECTED);
        }
        return true;
    }

}
