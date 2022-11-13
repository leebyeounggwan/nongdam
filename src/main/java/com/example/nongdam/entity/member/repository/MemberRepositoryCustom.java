package com.example.nongdam.entity.member.repository;

import com.example.nongdam.entity.member.entity.Member;

public interface MemberRepositoryCustom {
    public Member selectMemberByIdFetch(int id);
}
