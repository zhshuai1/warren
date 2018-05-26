package com.zebrait.strategy;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class NewStockStrategy implements Strategy {

    @Override
    public boolean checkCandidate(Stock stock) {

        List<Price> histories = stock.getHistories();
        log.info("The size of stock {} is {}", stock.getCode(), histories.size());
        if (histories.size() > 100) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkBuy(Stock stock) {
        return false;
    }

    @Override
    public Stock buy(List<Stock> candidates) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkSell(Stock stock) {
        return false;
    }

}
