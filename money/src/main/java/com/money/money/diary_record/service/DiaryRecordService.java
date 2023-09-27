package com.money.money.diary_record.service;

import com.money.money.auth.service.AuthenticationService;
import com.money.money.diary_record.domain.DailyTotalCostProjection;
import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.repository.DiaryRecordRepository;
import com.money.money.domain.MoneyUser;
import com.money.money.global.exception.UserNotAuthenticatedException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DiaryRecordService {
    private final DiaryRecordRepository repository;
    private final AuthenticationService authenticationService;

    public Page<DiaryRecord> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public DiaryRecord get(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Diary record not found."));
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DiaryRecord create(DiaryRecord diaryRecord) {
        Optional<UserDetails> user = authenticationService.getAuthenticationByContext();
        if (user.isPresent()) {
            MoneyUser moneyUser = (MoneyUser) user.get();
            diaryRecord.setUpdatedBy(moneyUser);
        }
        return repository.saveAndFlush(diaryRecord);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DiaryRecord update(Long id, DiaryRecord newDiaryRecordData) {
        return repository.findById(id)
            .map(diaryRecord -> {
                diaryRecord.setName(newDiaryRecordData.getName());
                diaryRecord.setDate(newDiaryRecordData.getDate());
                diaryRecord.setIcon(newDiaryRecordData.getIcon());
                diaryRecord.setType(newDiaryRecordData.getType());
                diaryRecord.setPrice(newDiaryRecordData.getPrice());
                return repository.saveAndFlush(diaryRecord);
            })
            .orElseThrow(() -> new ResourceAccessException("DiaryRecord not found with id " + id));
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Range<LocalDate> getStartAndEndDates(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        return Range.closed(start, end);
    }

    public MoneyUser getAuthenticatedUser() {
        return authenticationService.getAuthenticationByContext()
            .map(userDetails -> (MoneyUser) userDetails)
            .orElseThrow(() -> new UserNotAuthenticatedException(
                "User is not authenticated or not found in database"));
    }

    public List<DailyTotalCostProjection> getDailyTotalCostForUserAndDateRange(int year, int month) {
        var user = getAuthenticatedUser();
        var range = getStartAndEndDates(year, month);
        return repository.calculateDailyTotalCostForUserAndDateRange(
            user,
            range.getLowerBound().getValue().orElse(null),
            range.getUpperBound().getValue().orElse(null)
        );
    }

    public List<DiaryRecord> getByMonth(int year, int month) {
        var user = getAuthenticatedUser();
        var range = getStartAndEndDates(year, month);
        return repository.findByUserIdAndDateRange(
            user,
            range.getLowerBound().getValue().orElse(null),
            range.getUpperBound().getValue().orElse(null)
        );
    }


}
