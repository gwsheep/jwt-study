package com.jstudy.jwtandauthorization.dto;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class RequestVO {

    @NonNull
    private String id;

    @NonNull
    private String secretInfo;

}
