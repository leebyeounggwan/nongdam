package com.example.nongdam.repository;

import com.example.nongdam.entity.AccountBook;
import com.example.nongdam.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import static com.example.nongdam.entity.QMember.member;
import static com.example.nongdam.entity.QCrop.crop;
import static com.example.nongdam.entity.QAccountBook.accountBook;

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
