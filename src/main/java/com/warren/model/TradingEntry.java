package com.warren.model;


import com.warren.strategy.Strategy;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trading")
@Entity
public class TradingEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private Date date;
    private double price;
    private long quantity;
    @Enumerated(EnumType.STRING)
    private TradingType tradingType;
    private String strategy;

    public enum TradingType {
        CANDIDATE,DECANDIDATE, BUY, SELL
    }

    public enum TradingStatus {
        CANDIDATE, BOUGHT, FREE
    }
}