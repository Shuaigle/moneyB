package com.money.money.diary_record.service;


import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.repository.DiaryRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DiaryRecordServiceTest {

    @MockBean
    private DiaryRecordRepository repository;

    private DiaryRecordService diaryRecordService;

    @Test
    public void getDiaryRecordTest() {
        DiaryRecord expected = new DiaryRecord();
        expected.setId(1L);
        expected.setName("Test Name");
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(expected));

        diaryRecordService = new DiaryRecordService(repository);
        DiaryRecord actual = diaryRecordService.get(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void createDiaryRecordTest() {
        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setName("Test Name");
        diaryRecord.setDate(LocalDate.now());
        diaryRecord.setIcon("Icon");
        diaryRecord.setType("Type");
        diaryRecord.setPrice(BigDecimal.TEN);
        diaryRecord.setCreatedAt(LocalDateTime.now());

        when(repository.saveAndFlush(diaryRecord)).thenReturn(diaryRecord);

        diaryRecordService = new DiaryRecordService(repository);
        DiaryRecord actual = diaryRecordService.create(diaryRecord);
        assertEquals(diaryRecord, actual);
    }

    @Test
    public void updateDiaryRecordTest() {
        DiaryRecord existing = new DiaryRecord();
        existing.setId(1L);
        existing.setName("Old Name");

        DiaryRecord newDiaryRecord = new DiaryRecord();
        newDiaryRecord.setName("New Name");

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(repository.saveAndFlush(existing)).thenReturn(newDiaryRecord);

        diaryRecordService = new DiaryRecordService(repository);
        DiaryRecord actual = diaryRecordService.update(1L, newDiaryRecord);
        assertEquals(newDiaryRecord, actual);
    }
}
