package com.example.nongdam.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequestDto {

    private String email;

    private String password;
}
