package com.zebrait.strategy.continuousgrowing;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;
import com.zebrait.strategy.Strategy;
import com.zebrait.util.TimeSeriesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class ContinuousGrowingStrategy implements Strategy {
    private final double increase;
    private final double maxFall;
    private final int days;

    @Override
    public boolean checkCandidate(Stock stock) {
        List<Price> histories = stock.getHistories();
        String code = stock.getCode();
        log.info("Checking status for {}...", code);
        int startIndex = histories.size() - days;
        int endIndex = histories.size() - 1;
        // The stock is too new;
        if (histories.size() < days) {
            return false;
        }

        // increase during these days should be a large value;
        double startPrice = histories.get(startIndex).getClose();
        double endPrice = histories.get(endIndex).getClose();
        if ((endPrice - startPrice) / startPrice * 100 < increase) {
            return false;
        }

        // max fall should be small
        if (-TimeSeriesUtil.maxFall(histories, startIndex, endIndex) * 100 > maxFall) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkBuy(Stock stock) {
        return false;
    }

    @Override
    public boolean checkSell(Stock stock) {
        return false;
    }

    @Override
    public Stock buy(List<Stock> candidates) {
        return null;
    }
}
