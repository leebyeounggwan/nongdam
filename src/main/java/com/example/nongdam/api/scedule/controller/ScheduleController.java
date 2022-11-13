package com.example.nongdam.api.scedule.controller;

import com.example.nongdam.api.scedule.dto.request.ScheduleRequestDto;
import com.example.nongdam.api.scedule.dto.response.ScheduleResponseDto;
import com.example.nongdam.global.exception.WrongArgumentException;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public List<ScheduleResponseDto> getSchedules(@AuthenticationPrincipal MemberDetail detail){
        return scheduleService.findScheduleOfWeek(detail.getMember());
    }
    @GetMapping("/schedule/{yearMonth}")
    public List<ScheduleResponseDto> getMonthSchedules(@AuthenticationPrincipal MemberDetail detail,@PathVariable String yearMonth) throws WrongArgumentException {
        String[] tmp = yearMonth.split("-");
        if(tmp.length != 2)
            throw new WrongArgumentException("잘못된 요청입니다.","Year-Month");
        int year;
        int month;
        try {
            year = Integer.parseInt(tmp[0]);
            month = Integer.parseInt(tmp[1]);
        }catch(NumberFormatException e){
            throw new WrongArgumentException("잘못된 요청입니다.","Year-Month");
        }
        return scheduleService.findScheduleOfMonth(detail.getMember(),year,month);
    }
    @GetMapping("/schedule/today")
    public List<ScheduleResponseDto> getDaySchedules(@AuthenticationPrincipal MemberDetail detail){
        return scheduleService.findScheduleOfDay(detail.getMember());
    }
    @PostMapping("/schedule")
    public ScheduleResponseDto saveSchedule(@RequestBody ScheduleRequestDto dto, @AuthenticationPrincipal MemberDetail detail) throws WrongArgumentException {
        return scheduleService.save(detail.getMember(),dto);
    }
    @PutMapping("/schedule/{scheduleId}")
    public ScheduleResponseDto editSchedule(@PathVariable Long scheduleId,@RequestBody ScheduleRequestDto dto) throws WrongArgumentException {
        return scheduleService.save(scheduleId,dto);
    }
    @DeleteMapping("/schedule/{scheduleId}")
    public void deleteSchedule(@PathVariable Long scheduleId){
        scheduleService.delete(scheduleId);
    }

    @ExceptionHandler({NumberFormatException.class,IndexOutOfBoundsException.class})
    public ResponseEntity<String> badRequest(){
        return new ResponseEntity("요청데이터가 잘못되었습니다.", HttpStatus.BAD_REQUEST);
    }
}
