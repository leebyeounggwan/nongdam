package com.example.nongdam.service;

import com.example.nongdam.FinalValue;
import com.example.nongdam.dto.request.AccountRequestDto;
import com.example.nongdam.dto.response.AccountResponseDto;
import com.example.nongdam.dto.response.CircleChartDto;
import com.example.nongdam.dto.response.LineChartDataDto;
import com.example.nongdam.dto.response.LineChartDto;
import com.example.nongdam.entity.AccountBook;
import com.example.nongdam.entity.Member;
import com.example.nongdam.enums.AccountType;
import com.example.nongdam.repository.AccountBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final AccountBookRepository accountBookRepository;

    // 장부 기록
    public AccountResponseDto save(Member member, AccountRequestDto dto){
        return new AccountResponseDto(accountBookRepository.save(dto.build(member)));
    }

    // 장부 수정
    public AccountResponseDto save(long id, AccountRequestDto dto){
        AccountBook book = accountBookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("장부 정보를 찾을 수 없습니다."));
        book.update(dto);
        return new AccountResponseDto(accountBookRepository.save(book));
    }

    // 장부 삭제
    @Transactional
    public void delete(long id){
        accountBookRepository.deleteById(id);
    }

    // 장부 월간 조회
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findByMonth(Member member, int year, int month){
        YearMonth yearMonth = YearMonth.of(year,month);
        LocalDate startTime = LocalDate.of(year,month,1);
        LocalDate endTime = yearMonth.atEndOfMonth();
        startTime = startTime.minusDays(FinalValue.getBackDayOfWeekValue(startTime.getDayOfWeek()));
        endTime = endTime.plusDays(FinalValue.getForwardDayOfWeekValue(endTime.getDayOfWeek()));
        List<AccountBook> books = accountBookRepository.findAccountBookByMonth(member.getId(),startTime,endTime);
        return convertResponse(books);
    }

    private List<AccountResponseDto> convertResponse(List<AccountBook> list){
        List<AccountResponseDto> ret = new ArrayList<>();
        list.stream().forEach(e->ret.add(new AccountResponseDto(e)));
        return ret;
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDto> findByLimits(Member member,int maxResult){
        List<AccountBook> books = accountBookRepository.selectAccountBookByMaxResult(member,maxResult);
        return convertResponse(books);
    }

    public LineChartDto getResultsMonth(Member m){
        LocalDate now = LocalDate.now();
        LocalDate preDate = LocalDate.of(now.getYear(),now.getMonthValue(),1).minusMonths(5L);

        List<Object[]> incomes = accountBookRepository.incomeOfMonth(m.getId(),preDate);
        List<Object[]> spands = accountBookRepository.spandOfMonth(m.getId(),preDate);

        LineChartDto ret = new LineChartDto();

        LineChartDataDto income = LineChartDataDto.builder().name("매출").build();
        LineChartDataDto spand = LineChartDataDto.builder().name("비용").build();
        LineChartDataDto rawIncome = LineChartDataDto.builder().name("순이익").build();
        while(preDate.isBefore(now)|| preDate.isEqual(now)){
            int year = preDate.getYear();
            int month = preDate.getMonthValue();
            Object[] incomeData = incomes.stream().filter(e ->(int) e[0] == year && (int) e[1] == month)
                    .findFirst().orElse(null);
            Object[] spandData = spands.stream().filter(e ->(int) e[0] == year && (int) e[1] == month)
                    .findFirst().orElse(null);
            ret.addLabel(preDate.format(DateTimeFormatter.ofPattern("yyyy.MM")));
            int incomeValue = incomeData ==null?0:Integer.parseInt(incomeData[2].toString());
            int spandValue = spandData==null?0:Integer.parseInt(spandData[2].toString());
            income.addData(incomeValue);
            spand.addData(spandValue);
            rawIncome.addData(incomeValue - spandValue);
            preDate = preDate.plusMonths(1L);
        }
        ret.addData(income);
        ret.addData(spand);
        ret.addData(rawIncome);
        return ret;
    }
    public LineChartDto getResultsYear(Member m){

        LocalDate now = LocalDate.now();
        LocalDate pastYear = now.minusYears(5L);
        List<Object[]> incomes = accountBookRepository.incomeOfYear(m.getId(),pastYear.getYear());
        List<Object[]> spands = accountBookRepository.spandOfYear(m.getId(), pastYear.getYear());

        LineChartDto ret = new LineChartDto();
        LineChartDataDto income = LineChartDataDto.builder().name("매출").build();
        LineChartDataDto spand = LineChartDataDto.builder().name("비용").build();
        LineChartDataDto rawIncome = LineChartDataDto.builder().name("순이익").build();

        while(pastYear.isBefore(now) || pastYear.isEqual(now)){
            int year = pastYear.getYear();
            Object[] incomeData = incomes.stream().filter(e ->(int) e[0] == year )
                    .findFirst().orElse(null);
            Object[] spandData = spands.stream().filter(e ->(int) e[0] == year)
                    .findFirst().orElse(null);
            ret.addLabel(Integer.toString(pastYear.getYear()));
            int incomeValue = incomeData==null?0:Integer.parseInt(incomeData[1].toString());
            int spandValue = spandData==null?0:Integer.parseInt(spandData[1].toString());
            income.addData(incomeValue);
            spand.addData(spandValue);
            rawIncome.addData(incomeValue - spandValue);
            pastYear = pastYear.plusYears(1L);
        }
        ret.addData(income);
        ret.addData(spand);
        ret.addData(rawIncome);
        return ret;
    }

    public CircleChartDto getIncomeResult(Member m){
        List<Object[]> datas = accountBookRepository.getIncomeData(m.getId(), LocalDate.now().getYear());
        return makeCircleChart(datas);
    }
    public CircleChartDto getExpenseResult(Member m){
        List<Object[]> datas = accountBookRepository.getExpenseData(m.getId(), LocalDate.now().getYear());
        return makeCircleChart(datas);
    }
    private CircleChartDto makeCircleChart(List<Object[]> datas){
        CircleChartDto dto = new CircleChartDto();
        for(Object[] data : datas){
            dto.addLabel(AccountType.values()[(int)data[0]].name());
            dto.addData(Long.parseLong(data[1].toString()));
        }
        return dto;
    }

}
