package com.money.money.diary_record.repository;

import com.money.money.diary_record.domain.DiaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRecordRepository extends JpaRepository<DiaryRecord, Long> {
    @Query("SELECT dr FROM DiaryRecord dr WHERE MONTH(dr.date) = :month AND YEAR(dr.date) = :year")
    List<DiaryRecord> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
