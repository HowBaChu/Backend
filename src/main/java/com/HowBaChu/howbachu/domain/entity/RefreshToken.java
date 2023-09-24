package com.HowBaChu.howbachu.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash(value = "RefreshToken")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String key;
    private String value;
    @TimeToLive
    private int expiration;

    public void updateValue(String newRefreshToken) {
        this.value = newRefreshToken;
    }
}

