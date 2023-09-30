package com.money.money.diary_record.mock;

import com.money.money.diary_record.domain.DailyTotalCostProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MockDailyTotalCostProjection(LocalDate date, BigDecimal totalCost) implements DailyTotalCostProjection {
}


