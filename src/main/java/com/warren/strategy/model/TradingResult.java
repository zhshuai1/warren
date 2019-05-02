package com.warren.strategy.model;

import lombok.*;

import java.util.Date;

@Getter@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingResult {
    private boolean success;
    private double price;
    private Date time;
}
