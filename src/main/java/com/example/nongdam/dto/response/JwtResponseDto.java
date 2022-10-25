package com.example.nongdam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String refreshToken;


}
