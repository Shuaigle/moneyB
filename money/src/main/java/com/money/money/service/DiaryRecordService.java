package com.money.money.service;

import com.money.money.domain.DiaryRecord;
import com.money.money.repository.DiaryRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class DiaryRecordService {
    private final DiaryRecordRepository repository;

    public DiaryRecord get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Diary record not found."));
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DiaryRecord create(DiaryRecord diaryRecord) {
        return repository.save(diaryRecord);
    }
}
