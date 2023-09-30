package com.money.money.diary_record.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyTotalCostProjection {

    LocalDate date();

    BigDecimal totalCost();
}
