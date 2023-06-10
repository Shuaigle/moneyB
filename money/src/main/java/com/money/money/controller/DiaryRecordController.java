package com.money.money.controller;

import com.money.money.diary_record.domain.DiaryRecord;
import com.money.money.diary_record.service.DiaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary-records")
@RequiredArgsConstructor
public class DiaryRecordController {
    private final DiaryRecordService service;

    @GetMapping("/{id}")
    public ResponseEntity<DiaryRecord> get(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/create")
    public ResponseEntity<DiaryRecord> create(@RequestBody DiaryRecord diaryRecord){
        return ResponseEntity.ok(service.create(diaryRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryRecord> update(@RequestBody DiaryRecord diaryRecord,
                                              @PathVariable("id") Long id){
        return ResponseEntity.ok(service.update(id, diaryRecord));
    }

}
