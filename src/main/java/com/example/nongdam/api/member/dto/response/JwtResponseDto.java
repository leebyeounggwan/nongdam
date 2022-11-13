package com.example.nongdam.api.member.dto.response;

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
