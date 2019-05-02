package com.warren.strategy.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinuteDataEntry {
    private long volume;
    private double price;
    private double avg_price;
    private String date;
    private double prevclose;
}
