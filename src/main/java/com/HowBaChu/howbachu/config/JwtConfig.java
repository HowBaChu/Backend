package com.HowBaChu.howbachu.config;

import com.HowBaChu.howbachu.core.factory.YamlLoadFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * jwt secret key 관리 부분
 */
@Configuration
@PropertySource(value = {"classpath:secrets.yml"}, factory = YamlLoadFactory.class)
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@ToString
@Component
public class JwtConfig {
    private String secretKey;
    private int accessExpirationTime;
    private int refreshExpirationTime;
}
