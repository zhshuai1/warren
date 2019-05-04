package com.warren.strategy.continuousgrowing;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.Strategy;
import com.warren.strategy.common.*;
import com.warren.strategy.model.TradingResult;
import com.warren.util.series.SeriesUtil;

import java.util.List;

/**
 * some observation:
 * 1. It is continuous growing. If some day it rose a lot, then time to leave.
 * 2. Never buy those who had a continuous tremendous increase;
 */

public class ContinuousGrowingStrategy implements Strategy {
    private int checkedDays;
    private double rise;
    private double maxFall;
    private double maxRise;
    private double expectGains;
    private double bearedLoss;
    private int maxDays;
    private CandidateStrategy candidateStrategy;
    private BuyStrategy buyStrategy;
    private SellStrategy sellStrategy;

    public ContinuousGrowingStrategy() {
        this.checkedDays = 25;
        this.rise = 0.2;
        this.maxFall = -0.1;
        this.expectGains = 0.05;
        this.bearedLoss = -0.05;
        this.maxDays = 3;
        this.buyStrategy = new ModerateOpenBuyStrategy();
        this.sellStrategy = GainLossDaysSellStrategy.builder().expectedGain(expectGains)
                .bearedLoss(bearedLoss)
                .maxDays(maxDays).build();
    }

    @Override
    public boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index) {
        if (index < checkedDays) {
            return false;
        }
        Double initPrice = stockDayInfos.get(index - checkedDays).getAuthority();
        Double yesterdayPrice = stockDayInfos.get(index - 1).getAuthority();
        if (initPrice == null || yesterdayPrice == null) {
            return false;
        }
        if ((yesterdayPrice - initPrice) / initPrice > rise &&
                SeriesUtil.maxFall(stockDayInfos, index - checkedDays, index - 1) > maxFall) {
            return true;
        }
        return false;
    }

    @Override
    public TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        return buyStrategy.checkBuy(stockDayInfos, index, lastTradingEntry);
    }

    @Override
    public TradingResult checkSell(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        return sellStrategy.checkSell(stockDayInfos, index, lastTradingEntry);
    }
}
