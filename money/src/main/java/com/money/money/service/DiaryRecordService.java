package com.money.money.service;

import com.money.money.domain.DiaryRecord;
import com.money.money.repository.DiaryRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DiaryRecordService {
    private final DiaryRecordRepository repository;

    public DiaryRecord get(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Diary record not found."));
    }
}
