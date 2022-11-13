package com.example.nongdam.entity.accountBook.repository;

import com.example.nongdam.entity.accountBook.entity.AccountBook;
import com.example.nongdam.entity.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.example.nongdam.entity.accountBook.entity.QAccountBook.accountBook;

public class AccountBookRepositoryImpl implements AccountBookRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public AccountBookRepositoryImpl(JPAQueryFactory queryFactory){
        //super(AccountBook.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<AccountBook> selectAccountBookByMaxResult(Member member, int maxResult) {
        return queryFactory.selectFrom(accountBook)
                .where(accountBook.member.id.eq(member.getId()))
                .orderBy(accountBook.date.desc())
                .limit(maxResult)
                .fetch();
    }




}
