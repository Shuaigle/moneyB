package com.money.money.diary_record.mock;

import com.money.money.diary_record.domain.DailyTotalCostProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MockDailyTotalCostProjection implements DailyTotalCostProjection {
    private final LocalDate date;
    private final BigDecimal totalCost;

    public MockDailyTotalCostProjection(LocalDate date, BigDecimal totalCost) {
        this.date = date;
        this.totalCost = totalCost;
    }

    @Override
    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public BigDecimal getTotalCost() {
        return this.totalCost;
    }
}


