package com.yschoi.login.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenAndId {
    private String bearerToken;
    private String userId;
    private String name;
}
