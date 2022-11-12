package com.example.nongdam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRequestDto {

    private String nickname;
    private String address;
    private int countryCode;
    private List<Integer> crops;
}