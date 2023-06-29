package com.money.money.diary_record.repository;

import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.domain.MoneyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRecordRepository extends JpaRepository<DiaryRecord, Long> {
    @Query("SELECT dr FROM DiaryRecord dr WHERE dr.updatedBy = :updatedBy " +
            "AND dr.date >= :startDate AND dr.date <= :endDate")
    List<DiaryRecord> findByUserIdAndDateRange(
            @Param("updatedBy") MoneyUser user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
