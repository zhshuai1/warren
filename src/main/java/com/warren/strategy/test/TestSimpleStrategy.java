package com.warren.strategy.test;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.Strategy;
import com.warren.strategy.model.MinuteDataEntry;
import com.warren.strategy.model.TradingResult;
import com.warren.util.DateUtil;

import java.util.Date;
import java.util.List;

public class TestSimpleStrategy implements Strategy {
    @Override
    public boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index) {
        if (stockDayInfos.get(index).getDelta() > 0.09) {
            return true;
        }
        return false;
    }

    @Override
    public TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        List<MinuteDataEntry> minuteDataEntries = parse(stockDayInfos.get(index).getMinute());
        MinuteDataEntry open = minuteDataEntries.get(0);
        double preClose = open.getAvg_price();
        double openPrice = open.getPrice();
        double delta = openPrice / preClose - 1;
        if (delta > 0 && delta < 0.01) {
            return TradingResult.builder().price(openPrice)
                    .success(true)
                    .time(index2Time(open.getDate(), 0)).build();
        } else return TradingResult.builder().success(false).build();
    }

    @Override
    public TradingResult checkSell(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        // T+1
        if (!DateUtil.isEarlierThanYesterday(lastTradingEntry.getDate())) {
            return TradingResult.builder().success(false).build();
        }
        // the stock should be bought before sold
        if (!TradingEntry.TradingType.BUY.equals(lastTradingEntry.getTradingType())) {
            return TradingResult.builder().success(false).build();
        }
        List<MinuteDataEntry> minuteDataEntries = parse(stockDayInfos.get(index).getMinute());

        double priceWhenBought = lastTradingEntry.getPrice();
        Date boughtDate = lastTradingEntry.getDate();
        MinuteDataEntry open = minuteDataEntries.get(0);
        if (open.getPrice() > priceWhenBought * 10.5 || DateUtil.daysAgo(boughtDate) > 3) {
            return TradingResult.builder().success(true)
                    .time(index2Time(open.getDate(), 0))
                    .price(open.getPrice()).build();
        }
        return TradingResult.builder().success(false).build();

    }
}
