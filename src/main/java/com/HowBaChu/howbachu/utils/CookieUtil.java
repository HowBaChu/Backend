package com.HowBaChu.howbachu.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieUtil {

    public void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void setCookie(HttpServletResponse response, String key, String value, int expiredTime) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiredTime);  // 2주
        response.addCookie(cookie);
        log.info("만료 기한: "+ expiredTime);
    }

    public int getRemainTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
        Duration duration = Duration.between(now.toLocalTime(), endOfDay);
        return Integer.parseInt(duration.getSeconds()+"");
    }
}
