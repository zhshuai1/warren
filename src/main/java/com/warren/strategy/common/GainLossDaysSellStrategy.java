package com.warren.strategy.common;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.model.MinuteDataEntry;
import com.warren.strategy.model.TradingResult;
import com.warren.util.DateUtil;
import com.warren.util.parse.StockParser;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
public class GainLossDaysSellStrategy implements SellStrategy {
    private double expectedGain;
    private double bearedLoss;
    private int maxDays;

    public GainLossDaysSellStrategy() {
        this.expectedGain = 0.05;
        this.bearedLoss = -0.05;
        this.maxDays = 3;
    }

    @Override
    public TradingResult checkSell(List<StockDayInfo> stockDayInfos, int index, TradingEntry lastTradingEntry) {
        Date currentDate = stockDayInfos.get(index).getDate();
        // T+1
        if (DateUtil.diffDays(currentDate, DateUtil.parseFromFull(lastTradingEntry.getDate())) < 1) {
            return TradingResult.builder().success(false).build();
        }
        // the stock should be bought before sold
        if (!TradingEntry.TradingType.BUY.equals(lastTradingEntry.getTradingType())) {
            return TradingResult.builder().success(false).build();
        }
        List<MinuteDataEntry> minuteDataEntries = StockParser.parse(stockDayInfos.get(index).getMinute());

        double priceWhenBought = lastTradingEntry.getPrice();
        Date boughtDate = DateUtil.parseFromFull(lastTradingEntry.getDate());
        MinuteDataEntry open = minuteDataEntries.get(0);
        if (DateUtil.diffDays(currentDate, boughtDate) > maxDays) {
            return TradingResult.builder().success(true)
                    .time(StockParser.index2Time(open.getDate(), 0))
                    .price(open.getPrice()).build();
        } else {
            for (int i = 0; i < minuteDataEntries.size(); ++i) {
                double currentPrice = minuteDataEntries.get(i).getPrice();
                // currentPrice!=0 is to filter out the bad data
                // to ignore some of the check points, only need to set them proper:
                // 1. maxDays -> 10000;
                // 2. expectedGain -> 100
                // 3. bearedLoss -> -100
                if (currentPrice != 0 && currentPrice > priceWhenBought * (1 + expectedGain) || currentPrice < priceWhenBought * (1 + bearedLoss)) {
                    return TradingResult.builder().success(true)
                            .time(StockParser.index2Time(open.getDate(), i))
                            .price(currentPrice).build();
                }
            }
        }
        return TradingResult.builder().success(false).build();
    }

}
