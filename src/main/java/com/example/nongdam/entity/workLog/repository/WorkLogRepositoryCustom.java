package com.example.nongdam.entity.workLog.repository;

import com.querydsl.core.Tuple;

public interface WorkLogRepositoryCustom {

    public Tuple selectNextWorkLog(int memberId, long workLogId) throws IndexOutOfBoundsException;
    public Tuple selectPreviousWorkLog(int memberId, long workLogId) throws IndexOutOfBoundsException;
}
