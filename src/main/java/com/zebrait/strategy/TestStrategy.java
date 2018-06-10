package com.zebrait.strategy;

import com.zebrait.model.Stock;

import java.util.List;

public class TestStrategy implements Strategy {
    @Override
    public boolean checkCandidate(Stock stock) {
        if("sh600030".equals(stock.getCode())){
            return  true;
        }
        return false;
    }

    @Override
    public boolean checkBuy(Stock stock) {
        if("sh600030".equals(stock.getCode())){
            return  true;
        }
        return false;
    }

    @Override
    public boolean checkSell(Stock stock) {
        if("sh600030".equals(stock.getCode())){
            return  true;
        }
        return false;
    }

    @Override
    public Stock buy(List<Stock> candidates) {
        return null;
    }
}
