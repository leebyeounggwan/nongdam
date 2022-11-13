package com.example.nongdam.api.workLog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WorkTimeRateDto {
    private int rate;

    private String rateText;
}
