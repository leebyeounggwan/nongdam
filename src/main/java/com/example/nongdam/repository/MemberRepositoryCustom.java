package com.example.nongdam.repository;

import com.example.nongdam.entity.Member;

public interface MemberRepositoryCustom {
    public Member selectMemberByIdFetch(int id);
}
