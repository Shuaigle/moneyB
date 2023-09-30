package com.money.money.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthRegisterRequest implements Serializable {

    private String username;
    private String email;
    @ToString.Exclude
    private String password;
    @ToString.Exclude
    private String token;
}
