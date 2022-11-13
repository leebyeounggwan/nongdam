package com.example.nongdam.controller;

import com.example.nongdam.dto.response.CircleChartDto;
import com.example.nongdam.dto.response.LineChartDto;
import com.example.nongdam.dto.response.WorkTimeRateDto;
import com.example.nongdam.security.MemberDetail;
import com.example.nongdam.service.AccountBookService;
import com.example.nongdam.service.WorkLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ResultController {
    private final AccountBookService accountBookService;

    private final WorkLogService workLogService;
    private final Environment env;

    @GetMapping("/totalharvest/{isYear}")
    //@Operation(summary = "전체 수확량(막대그래프,전체데이터)")
    public LineChartDto getHarvestResult(@AuthenticationPrincipal MemberDetail detail, @PathVariable("isYear")String isYear){
        return isYear.equals("year")?workLogService.getHarvestYearData(detail.getMember()):workLogService.getHarvestMonthData(detail.getMember());
    }

    @GetMapping("/sales/{isYear}")
    //@Operation(summary = "판매,지출,순이익(막대그래프,전체데이터)")
    public LineChartDto getAccountResult(@AuthenticationPrincipal MemberDetail detail,@PathVariable("isYear")String isYear){
        return isYear.equals("year")?accountBookService.getResultsYear(detail.getMember()):accountBookService.getResultsMonth(detail.getMember());
    }

    @GetMapping("/income")
    //@Operation(summary = "판매(Pie그래프,1년)")
    public CircleChartDto getIncomeOfYear(@AuthenticationPrincipal MemberDetail detail){
        return accountBookService.getIncomeResult(detail.getMember());
    }

    @GetMapping("/expense")
    //@Operation(summary = "지출(Pie그래프,1년)")
    public CircleChartDto getExpenseOfYear(@AuthenticationPrincipal MemberDetail detail){
        return accountBookService.getExpenseResult(detail.getMember());
    }
    @GetMapping("/worktime")
    //@Operation(summary = "분기별 작업 시간 데이터(막대그래프, 금년, 작년)")
    public LineChartDto getWorkTimeResult(@AuthenticationPrincipal MemberDetail detail){
        return workLogService.getWorkTimeData(detail.getMember());
    }
    @GetMapping("/worktime/rate")
    //@Operation(summary = "분기별 작업 시간 비율 데이터")
    public WorkTimeRateDto getWorkTimeRate(@AuthenticationPrincipal MemberDetail detail){
        return workLogService.getWorkingRate(detail.getMember());
    }
//    @GetMapping("/profile")
//    @Operation(summary = "무중단 배포 확인용(무시해도됨)")
//    public String getProfile () {
//        return Arrays.stream(env.getActiveProfiles())
//                .findFirst()
//                .orElse("");
//    }
}
