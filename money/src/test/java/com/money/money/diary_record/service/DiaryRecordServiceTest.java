package com.money.money.diary_record.service;


import com.money.money.auth.service.AuthenticationService;
import com.money.money.diary_record.domain.DailyTotalCostProjection;
import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.mock.MockDailyTotalCostProjection;
import com.money.money.diary_record.repository.DiaryRecordRepository;
import com.money.money.domain.MoneyUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DiaryRecordServiceTest {

    @MockBean
    private DiaryRecordRepository repository;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private DiaryRecordService diaryRecordService;

    @Test
    public void getDiaryRecordTest() {
        DiaryRecord expected = new DiaryRecord();
        expected.setId(1L);
        expected.setName("Test Name");
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(expected));

        diaryRecordService = new DiaryRecordService(repository, authenticationService);
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

        diaryRecordService = new DiaryRecordService(repository, authenticationService);
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

        diaryRecordService = new DiaryRecordService(repository, authenticationService);
        DiaryRecord actual = diaryRecordService.update(1L, newDiaryRecord);
        assertEquals(newDiaryRecord, actual);
    }

    @Test
    public void testGetAll() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<DiaryRecord> expected = new PageImpl<>(List.of(new DiaryRecord()));

        when(repository.findAll(pageable)).thenReturn(expected);

        Page<DiaryRecord> actual = diaryRecordService.getAll(pageable);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetById() {
        DiaryRecord expected = new DiaryRecord();
        expected.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(expected));

        DiaryRecord actual = diaryRecordService.get(1L);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreate() {
        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setId(1L);

        when(authenticationService.getAuthenticationByContext())
                .thenReturn(Optional.of(new MoneyUser()));
        when(repository.saveAndFlush(diaryRecord)).thenReturn(diaryRecord);

        DiaryRecord actual = diaryRecordService.create(diaryRecord);

        assertEquals(diaryRecord, actual);
    }

    @Test
    public void testUpdate() {
        DiaryRecord existingRecord = new DiaryRecord();
        existingRecord.setId(1L);
        existingRecord.setName("Existing Record");

        DiaryRecord updatedRecord = new DiaryRecord();
        updatedRecord.setName("Updated Record");

        when(repository.findById(1L)).thenReturn(Optional.of(existingRecord));
        when(repository.saveAndFlush(existingRecord)).thenReturn(updatedRecord);

        DiaryRecord actual = diaryRecordService.update(1L, updatedRecord);

        assertEquals(updatedRecord, actual);
    }

    @Test
    public void testDelete() {
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> diaryRecordService.delete(1L));
    }

    @Test
    public void testGetStartAndEndDates() {
        int year = 2023;
        int month = 7;

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());

        Range<LocalDate> actual = diaryRecordService.getStartAndEndDates(year, month);

        assertEquals(start, actual.getLowerBound().getValue().orElse(null));
        assertEquals(end, actual.getUpperBound().getValue().orElse(null));
    }

    @Test
    public void testGetAuthenticatedUser() {
        MoneyUser expected = new MoneyUser();
        expected.setId(1L);

        when(authenticationService.getAuthenticationByContext())
                .thenReturn(Optional.of(expected));

        MoneyUser actual = diaryRecordService.getAuthenticatedUser();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetByMonth() {
        MoneyUser user = new MoneyUser();
        user.setId(1L);

        DiaryRecord expectedRecord = new DiaryRecord();
        expectedRecord.setId(1L);

        when(authenticationService.getAuthenticationByContext())
                .thenReturn(Optional.of(user));
        when(repository.findByUserIdAndDateRange(
                any(MoneyUser.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(expectedRecord));

        List<DiaryRecord> actual = diaryRecordService.getByMonth(2023, 7);

        assertEquals(List.of(expectedRecord), actual);
    }

    @Test
    public void testGetDailyTotalCostForUserAndDateRange() {
        MoneyUser user = new MoneyUser();
        user.setId(1L);

        DailyTotalCostProjection expectedProjection =
                new MockDailyTotalCostProjection(LocalDate.now(), BigDecimal.TEN);

        when(authenticationService.getAuthenticationByContext())
                .thenReturn(Optional.of(user));
        when(repository.calculateDailyTotalCostForUserAndDateRange(
                any(MoneyUser.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(expectedProjection));

        List<DailyTotalCostProjection> actual = diaryRecordService
                .getDailyTotalCostForUserAndDateRange(2023, 7);

        assertEquals(List.of(expectedProjection), actual);
    }
}
