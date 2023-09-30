package com.money.money.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Data
@RedisHash("SSOTokenResponse")
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SSOTokenResponse implements Serializable {
    private UUID token;
}
