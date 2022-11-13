package com.example.nongdam.entity.member.repository;

import com.example.nongdam.entity.member.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static com.example.nongdam.entity.member.entity.QMember.member;
import static com.example.nongdam.entity.crop.entity.QCrop.crop;

public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(JPAQueryFactory queryFactory){
        //super(AccountBook.class);
        this.queryFactory = queryFactory;
    }
    @Override
    public Member selectMemberByIdFetch(int id) {
        return queryFactory.selectFrom(member)
                .leftJoin(member.crops,crop).fetchJoin()
                .where(member.id.eq(id)).fetch().get(0);
    }
}
