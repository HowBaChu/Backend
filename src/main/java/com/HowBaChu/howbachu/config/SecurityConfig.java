package com.HowBaChu.howbachu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
/*    @Value("${spring.jwt.password}")
    private String SECRET_KEY;
    private final JwtProvider jwtProvider;
    private static final String[] PERMIT_URL_ARRAY = {
            *//* swagger v2 *//*
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            *//* swagger v3 *//*
            "/v3/api-docs/**",
            "/swagger-ui/**",
            *//* Login, Signup *//*
            "/api/members/login",
            "/api/members/sign-up"
    };*/

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()

                // 세션 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 로그인, 회원가입은 허용 -> 나머지 인증 인가 필요
                .authorizeRequests()
//                .antMatchers(PERMIT_URL_ARRAY).permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll() // 임시로 모두 허용
                .and()

                // 필터 적용
//                .addFilterBefore(new JwtFilter(jwtProvider,SECRET_KEY),
//                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}


