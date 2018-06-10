package com.zebrait.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class StockTradingEntry {
    private String code;
    private String strategy;
    @Enumerated(EnumType.STRING)
    private StockStatus status;
    private Date time;
    private double price;
}
