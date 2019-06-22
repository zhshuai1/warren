package com.warren.strategy.continuoussurge;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.strategy.Strategy;
import com.warren.strategy.common.BuyStrategy;
import com.warren.strategy.common.CandidateStrategy;
import com.warren.strategy.common.SellStrategy;
import com.warren.strategy.model.TradingResult;
import com.warren.util.series.SeriesUtil;

import java.util.List;

/**
 * It is continuously surging. Greate risk and opportunity.
 */
public class ContinuousSurgeStrategy implements Strategy {
    // checked days
    private int checkedDays;
    // at least in minRiseDays, the rise margin larger than minDelta
    private int minRiseDays;
    private double minDelta;
    // allowed max fall
    private double maxFall;
    private CandidateStrategy candidateStrategy;
    private BuyStrategy buyStrategy;
    private SellStrategy sellStrategy;

    public ContinuousSurgeStrategy() {
        this.checkedDays = 7;
        this.minRiseDays = 6;
        this.minDelta = 0.09;
        this.maxFall = -0.05;

    }

    @Override
    public boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index) {
        if (index < checkedDays) {
            return false;
        }
        int startIndex = index - checkedDays;
        int riseDays = 0;
        for (int i = startIndex; i < index; ++i) {
            if (minRiseDays >= 1) {
                if (SeriesUtil.isUpperBoundary(stockDayInfos, index - 1)) {
                    riseDays++;
                }
            } else if (stockDayInfos.get(i).getDelta() > minDelta) {
                riseDays++;
            }
        }
        return riseDays > minRiseDays && SeriesUtil.maxFall(stockDayInfos, startIndex, index - 1) > maxFall;
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
