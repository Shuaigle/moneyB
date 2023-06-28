package com.money.money.diary_record.service;

import com.money.money.auth.service.AuthenticationService;
import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.repository.DiaryRecordRepository;
import com.money.money.domain.MoneyUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public List<DiaryRecord> getByMonth(int year, int month) {
        Optional<UserDetails> userDetails = authenticationService.getAuthenticationByContext();
        if (userDetails.isPresent()) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
            return repository.findByUserIdAndDateRange(
                    (MoneyUser) userDetails.get(), start, end);
        }
        throw new UsernameNotFoundException(
                "Find diary records by month error: user is " +
                        "not authenticated or not found in database");
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


}
