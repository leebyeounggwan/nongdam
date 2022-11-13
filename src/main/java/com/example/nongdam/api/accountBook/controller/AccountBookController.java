package com.example.nongdam.api.accountBook.controller;

import com.example.nongdam.api.accountBook.dto.request.AccountRequestDto;
import com.example.nongdam.api.accountBook.dto.response.AccountResponseDto;
import com.example.nongdam.global.exception.WrongArgumentException;
import com.example.nongdam.global.security.MemberDetail;
import com.example.nongdam.entity.accountBook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService service;

    // 월간
    @GetMapping("/accountbook/{yearMonth}")
    public List<AccountResponseDto> findByMonth(@AuthenticationPrincipal MemberDetail detail, @PathVariable String yearMonth) throws WrongArgumentException {
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
        return service.findByMonth(detail.getMember(),year,month);
    }

    // 최근 10건
/*    @GetMapping("/accountbook")
    public List<AccountResponseDto> findByLimit(@AuthenticationPrincipal MemberDetail detail){
        return service.findByLimits(detail.getMember(),10);
    }*/

    @PostMapping("/accountbook")
    public AccountResponseDto saveAccount(@AuthenticationPrincipal MemberDetail detail, @RequestBody AccountRequestDto dto){
        return service.save(detail.getMember(),dto);
    }

    @PutMapping("/accountbook/{accountId}")
    public AccountResponseDto editAccount(@PathVariable Long accountId,@RequestBody AccountRequestDto dto){
        return service.save(accountId.longValue(),dto);
    }

    @DeleteMapping("/accountbook/{accountId}")
    public void deleteAccount(@PathVariable Long accountId){
        service.delete(accountId);
    }
}
