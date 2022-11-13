package com.example.nongdam.entity;

//import com.example.formproject.FinalValue;
//import com.example.formproject.dto.request.AccountRequestDto;
import com.example.nongdam.FinalValue;
import com.example.nongdam.dto.request.AccountRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AccountBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int type;

    @Column
    @PositiveOrZero(message = "가격")
    private int price;

    @Column
    private String memo;

    @Column
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    public void update(AccountRequestDto dto){
        this.type = dto.getType();
        this.price = dto.getPrice();
        this.memo = dto.getMemo();
        this.date = LocalDate.parse(dto.getDate(), FinalValue.DAY_FORMATTER);
    }
}
