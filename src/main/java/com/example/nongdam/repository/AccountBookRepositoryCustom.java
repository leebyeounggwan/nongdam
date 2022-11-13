package com.example.nongdam.repository;

import com.example.nongdam.entity.AccountBook;
import com.example.nongdam.entity.Member;
import com.querydsl.core.Tuple;

import java.util.List;

public interface AccountBookRepositoryCustom {

    public List<AccountBook> selectAccountBookByMaxResult(Member member, int maxResult);

}
