package com.example.nongdam.entity.accountBook.repository;

import com.example.nongdam.entity.accountBook.entity.AccountBook;
import com.example.nongdam.entity.member.entity.Member;
import com.querydsl.core.Tuple;

import java.util.List;

public interface AccountBookRepositoryCustom {

    public List<AccountBook> selectAccountBookByMaxResult(Member member, int maxResult);

}
