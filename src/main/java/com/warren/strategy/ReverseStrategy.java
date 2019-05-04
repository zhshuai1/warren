package com.warren.strategy;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.model.TradingResult;

import java.util.List;

public class ReverseStrategy implements Strategy {
    @Override
    public boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index) {
        return false;
    }

    @Override
    public TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        return null;
    }

    @Override
    public TradingResult checkSell(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        return null;
    }
}
