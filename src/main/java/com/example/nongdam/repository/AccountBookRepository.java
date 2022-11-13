package com.example.nongdam.repository;

import com.example.nongdam.entity.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountBookRepository extends JpaRepository<AccountBook,Long>, AccountBookRepositoryCustom {

    @Query("Select year(this_.date),month(this_.date),sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and this_.type < 3 and this_.date >= :date group by year(this_.date),month(this_.date)")
    public List<Object[]> incomeOfMonth(@Param("memberId") int memberId,@Param("date")LocalDate date);

    @Query("Select year(this_.date),month(this_.date),sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and this_.type > 2 and this_.date >= :date group by year(this_.date),month(this_.date)")
    public List<Object[]> spandOfMonth(@Param("memberId") int memberId,@Param("date")LocalDate date);

    @Query("Select year(this_.date),sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and this_.type < 3 and year(this_.date) >= :year group by year(this_.date)")
    public List<Object[]> incomeOfYear(@Param("memberId") int memberId,@Param("year")int year);

    @Query("Select year(this_.date),sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and this_.type > 2 and year(this_.date) >= :year  group by year(this_.date)")
    public List<Object[]> spandOfYear(@Param("memberId") int memberId,@Param("year")int year);

    @Query("Select min(this_.date),max(this_.date) from AccountBook  this_ where this_.member.id=:memberId")
    public List<LocalDate[]> getMinDate(@Param("memberId") int memberId);

    @Query("Select this_.type,sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and year(this_.date) = :year and this_.type < 3 group by this_.type order by sum(this_.price) desc")
    public List<Object[]> getIncomeData(@Param("memberId") int memberId,@Param("year")int year);

    @Query("Select this_.type,sum(this_.price) from AccountBook this_ where this_.member.id=:memberId and year(this_.date) = :year and this_.type > 2 group by this_.type order by sum(this_.price) desc")
    public List<Object[]> getExpenseData(@Param("memberId") int memberId,@Param("year")int year);

    @Query("Select this_ from AccountBook this_ where this_.member.id=:memberId and year(this_.date)=:year and month(this_.date)=:month order by this_.date desc")
    public List<AccountBook> findAccountBookByMonth(@Param("memberId")int memberId,@Param("year")int year,@Param("month")int month);

    @Query("Select this_ from AccountBook this_ where this_.member.id=:memberId and this_.date between :startTime and :endTime order by this_.date desc")
    public List<AccountBook> findAccountBookByMonth(@Param("memberId")int memberId,@Param("startTime") LocalDate startTime,@Param("endTime")LocalDate endTime);
}
