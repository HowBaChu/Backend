package com.HowBaChu.howbachu.config;

import com.HowBaChu.howbachu.exception.ExceptionHandleFilter;
import com.HowBaChu.howbachu.jwt.JwtFilter;
import com.HowBaChu.howbachu.jwt.JwtProvider;
import com.HowBaChu.howbachu.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .antMatchers("/v3/api-docs")
            .antMatchers("/swagger-resources/**")
            .antMatchers("/swagger-ui/**")
            .antMatchers("/webjars/**")
            .antMatchers("/swagger/**")
            .antMatchers("/api-docs/**")
            .antMatchers("/swagger-ui/**")
            ;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        security
            .httpBasic().disable()
            .csrf().disable()
            .cors();

            // 세션 비활성화
        security
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            // 로그인, 회원가입은 허용 -> 나머지 인증 인가 필요
        security
            .authorizeRequests()
            .antMatchers(
                "/api/v1/auth/signup",
                "/api/v1/auth/login",
                "/api/v1/member/email/{email}/exists",
                "/api/v1/member/username/{username}/exists"
                ).permitAll()
            .anyRequest().authenticated();

            // 필터 적용
        security
            .addFilterBefore(new ExceptionHandleFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtFilter(jwtProvider, refreshTokenRepository),
                UsernamePasswordAuthenticationFilter.class);


        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


