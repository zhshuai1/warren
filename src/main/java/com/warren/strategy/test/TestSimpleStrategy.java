package com.warren.strategy.test;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.Strategy;
import com.warren.strategy.common.*;
import com.warren.strategy.model.TradingResult;

import java.util.List;

public class TestSimpleStrategy implements Strategy {
    private CandidateStrategy candidateStrategy;
    private BuyStrategy buyStrategy;
    private SellStrategy sellStrategy;

    public TestSimpleStrategy() {
        this.buyStrategy = new ModerateOpenBuyStrategy();
        this.sellStrategy = new GainLossDaysSellStrategy();
    }

    @Override
    public boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index) {
        if (stockDayInfos.get(index - 1).getDelta() > 0.09) {
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
