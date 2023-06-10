package com.money.money.diary_record.repository;

import com.money.money.diary_record.domain.DiaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRecordRepository extends JpaRepository<DiaryRecord, Long> {
}
