package com.money.money.controller;

import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.service.DiaryRecordService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(
            summary = "Get paged diary records",
            description = "This operation will get the paged diary records.")
    @GetMapping
    public ResponseEntity<Page<DiaryRecord>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @Operation(
            summary = "Get monthly diary records",
            description = "This operation will get the specific year and month diary records.")
    @GetMapping("/{year}/{month}")
    ResponseEntity<List<DiaryRecord>> getByMonth(@PathVariable("year") int year,
                                                 @PathVariable("month") int month) {
        return ResponseEntity.ok(service.getByMonth(month, year));
    }

    @Operation(
            summary = "Get a diary record by id",
            description = "This operation will get the specific diary record by id.")
    @GetMapping("/{id}")
    public ResponseEntity<DiaryRecord> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @Operation(
            summary = "Create a new diary record",
            description = "This operation will create a diary record.")
    @PostMapping("/create")
    public ResponseEntity<DiaryRecord> create(@RequestBody DiaryRecord diaryRecord) {
        return ResponseEntity.ok(service.create(diaryRecord));
    }

    @Operation(
            summary = "Updated a diary record by id",
            description = "This operation will update a diary record by id.")
    @PutMapping("/{id}")
    public ResponseEntity<DiaryRecord> update(@RequestBody DiaryRecord diaryRecord,
                                              @PathVariable("id") Long id) {
        return ResponseEntity.ok(service.update(id, diaryRecord));
    }

}
