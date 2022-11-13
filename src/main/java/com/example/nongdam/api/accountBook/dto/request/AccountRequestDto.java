package com.example.nongdam.api.accountBook.dto.request;

import com.example.nongdam.entity.accountBook.entity.AccountBook;
import com.example.nongdam.entity.member.entity.Member;
import com.example.nongdam.global.FinalValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {
    private int type;
    private int price;
    private String memo;
    private String date;
    public AccountBook build(Member member){
        return AccountBook.builder()
                .date(LocalDate.parse(date, FinalValue.DAY_FORMATTER))
                .type(this.type)
                .price(this.price)
                .memo(this.memo)
                .member(member)
                .build();
    }
}
