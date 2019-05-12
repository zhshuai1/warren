package com.warren.strategy.common;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.model.MinuteDataEntry;
import com.warren.strategy.model.TradingResult;
import com.warren.util.DateUtil;
import com.warren.util.parse.StockParser;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class ModerateOpenBuyStrategy implements BuyStrategy {
    private int checkedMinutes;
    private double lowerBound;
    private double upperBound;

    public ModerateOpenBuyStrategy() {
        this.checkedMinutes = 20;
        this.lowerBound = 0;
        this.upperBound = 0.01;
    }

    @Override
    public TradingResult checkBuy(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        StockDayInfo todayInfo = stockDayInfos.get(index);
        List<MinuteDataEntry> minuteDataEntries = StockParser.parse(todayInfo.getMinute());
        if (minuteDataEntries == null) {
            String msg = String.format("The minute data is null: code: %s, date: %s", todayInfo.getCode(),
                    DateUtil.format(todayInfo.getDate()));
            System.out.print(msg);
            return TradingResult.builder().success(false).build();
        }
        MinuteDataEntry open = minuteDataEntries.get(0);
        double preClose = open.getPrevclose();
        //TODO: should check the trend for minutes data, not only one point?
        for (int i = 0; i < checkedMinutes; ++i) {
            double delta = minuteDataEntries.get(i).getPrice() / preClose - 1;
            if (delta > lowerBound && delta < upperBound) {
                StockDayInfo todayStockInfo = stockDayInfos.get(index);
                double currentPrice = minuteDataEntries.get(i).getPrice();
                double currentAuthority = todayStockInfo.getAuthority() * currentPrice / todayStockInfo.getClose();
                return TradingResult.builder().price(currentPrice)
                        .authority(currentAuthority)
                        .success(true)
                        .time(StockParser.index2Time(open.getDate(), 0)).build();
            }
        }
        return TradingResult.builder().success(false).build();
    }
}
