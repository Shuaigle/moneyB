package com.money.money.repository;

import com.money.money.domain.DiaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRecordRepository extends JpaRepository<DiaryRecord, Long> {
}
