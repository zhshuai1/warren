package com.zebrait.strategy;

import com.zebrait.model.Stock;

import java.util.List;

public interface Strategy {

    boolean checkCandidate(Stock stock);

    boolean checkBuy(Stock stock);

    boolean checkSell(Stock stock);

    Stock buy(List<Stock> candidates);
}
