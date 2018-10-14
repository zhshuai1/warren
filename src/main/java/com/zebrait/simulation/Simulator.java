package com.zebrait.simulation;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.CheckBuyResult;
import com.zebrait.strategy.CheckSellResult;
import com.zebrait.strategy.SimulationStrategy;

import java.util.LinkedList;
import java.util.List;

public final class Simulator {

    public static void simulateForOneStock(Stock fullStockData, SimulationStrategy strategy) {
        List<Price> prices = fullStockData.getHistories();
        List<Price> currentPrices = new LinkedList<>();
        Price firstPrice = prices.get(0);
        StockStatusEntry stockStatusEntry = StockStatusEntry.builder().stockStatus(StockStatus.FREE).build();
        for (int i = 0; i < prices.size() - 1; ++i) {
            Price price = prices.get(i);
            currentPrices.add(price);

            List<Price.MinuteData> todayPrices = prices.get(i + 1).getTodayPrices();
            List<Price.MinuteData> currentTodayPrices = new LinkedList<>();

            Stock stock = Stock.builder().code(firstPrice.getCode()).name(firstPrice.getName()).histories
                    (currentPrices).todayPrices(currentTodayPrices).build();
            for (Price.MinuteData minuteData : todayPrices) {
                currentTodayPrices.add(minuteData);
                if (stockStatusEntry.getStockStatus().equals(StockStatus.BOUGHT)) {
                    CheckSellResult result = strategy.checkSell(stock);
                    if (result.isShouldSell()) {
                        stockStatusEntry.setStockStatus(StockStatus.FREE);
                        //TODO: add one record in table;

                    }
                }
                if (stockStatusEntry.getStockStatus().equals(StockStatus.FREE) && strategy.checkCandidate(stock).isQualified()) {
                    CheckBuyResult result = strategy.checkBuy(stock);
                    if (result.isShouldBuy()) {
                        stockStatusEntry.setStockStatus(StockStatus.BOUGHT);
                        //TODO: add one record in table;

                    }
                }
            }
        }
    }
}
