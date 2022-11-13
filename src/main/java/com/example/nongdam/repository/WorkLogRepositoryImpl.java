package com.example.nongdam.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.example.nongdam.entity.QWorkLog.workLog;

@Repository
public class WorkLogRepositoryImpl implements WorkLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public WorkLogRepositoryImpl(JPAQueryFactory queryFactory){
        //super(AccountBook.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Tuple selectNextWorkLog(int memberId, long workLogId) throws IndexOutOfBoundsException {
        return queryFactory.select(workLog.id,workLog.title,workLog.date).from(workLog)
                .where(workLog.member.id.eq(memberId),workLog.id.gt(workLogId))
                .orderBy(workLog.id.asc())
                .limit(1).fetch().get(0);
    }

    @Override
    public Tuple selectPreviousWorkLog(int memberId, long workLogId) throws IndexOutOfBoundsException {
        return queryFactory.select(workLog.id,workLog.title,workLog.date).from(workLog)
                .where(workLog.member.id.eq(memberId),workLog.id.lt(workLogId))
                .orderBy(workLog.id.desc())
                .limit(1).fetch().get(0);
    }
}
