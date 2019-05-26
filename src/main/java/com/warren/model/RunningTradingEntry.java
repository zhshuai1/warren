package com.warren.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "run")
@Entity
public class RunningTradingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String date;
    private double price;
    // only valid when SELL
    private double boughtPrice;
    private double authority;
    private double boughtAuthority;
    private long quantity;
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;
    private String strategy;

    public enum TradingType {
        CANDIDATE, DECANDIDATE, BUY, SELL
    }

    public enum TradingStatus {
        CANDIDATE, BOUGHT, FREE
    }
}
