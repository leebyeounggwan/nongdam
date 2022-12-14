package com.example.nongdam.entity.member.repository;


import com.example.nongdam.entity.crop.entity.Crop;
import com.example.nongdam.entity.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Integer>, MemberRepositoryCustom {
    Optional<Member> findByEmail(String email);
    @Query("select this_.crops from Member this_ where this_.id=:id")
    List<Crop> findAllByMember(int id);

}
