package com.money.money.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
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
    private String date;

    @Column
    private String icon;

    @Column
    private String type;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private MoneyUser updatedBy;

}
