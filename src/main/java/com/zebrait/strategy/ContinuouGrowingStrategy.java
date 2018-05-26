package com.zebrait.strategy;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class ContinuouGrowingStrategy implements Strategy {
    @Override
    public boolean checkCandidate(Stock stock) {
        List<Price> histories = stock.getHistories();
        String code = stock.getCode();
        log.info("Checking status for {}...", code);
        if ("sh600030".equals(code)) {
            return true;
        } else {
            return false;
        }

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
