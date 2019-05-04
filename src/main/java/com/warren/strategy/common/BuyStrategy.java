package com.warren.strategy.common;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.model.TradingResult;

import java.util.List;

public interface BuyStrategy {
    TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry);
}
