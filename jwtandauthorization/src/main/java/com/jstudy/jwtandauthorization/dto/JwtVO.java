package com.jstudy.jwtandauthorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtVO {

    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expiry_date;


}
