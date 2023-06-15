package com.money.money.controller;

import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.service.DiaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/diary-records")
@RequiredArgsConstructor
public class DiaryRecordController {
    private final DiaryRecordService service;

    /// http://localhost:8081/diary-records?page=0&size=10&sort=date,desc
    @GetMapping
    public ResponseEntity<Page<DiaryRecord>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{year}/{month}")
    ResponseEntity<List<DiaryRecord>> getByMonth(@PathVariable("year") int year,
                                                 @PathVariable("month") int month) {
        return ResponseEntity.ok(service.getByMonth(month, year));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryRecord> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<DiaryRecord> create(@RequestBody DiaryRecord diaryRecord) {
        return ResponseEntity.ok(service.create(diaryRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryRecord> update(@RequestBody DiaryRecord diaryRecord,
                                              @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, diaryRecord));
    }

}
