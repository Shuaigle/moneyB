package com.money.money.diary_record.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.money.money.domain.MoneyUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "diaryrecord")
public class DiaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diaryrecord_id_seq")
    @SequenceGenerator(name = "diaryrecord_id_seq", sequenceName = "diaryrecord_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String icon;

    @Column
    private String type;

    @Column(nullable = false)
    private String name;

    @Column
    private BigDecimal price;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private MoneyUser updatedBy;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public enum TransactionType {
        INCOME, OUTCOME
    }

}
