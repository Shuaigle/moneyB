package com.money.money.auth.domain;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest implements Serializable {

    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    @ToString.Exclude
    private String password;
    @Nullable
    @ToString.Exclude
    private String token;

    public boolean isSSOLogin() {
        return StringUtils.isNotBlank(token);
    }
}
