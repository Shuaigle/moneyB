package com.money.money.controller;

import com.money.money.domain.DiaryRecord;
import com.money.money.service.DiaryRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}
