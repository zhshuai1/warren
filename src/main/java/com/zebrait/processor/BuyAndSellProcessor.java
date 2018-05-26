package com.zebrait.processor;

import com.zebrait.dataprovider.DataProvider;
import com.zebrait.dataprovider.EastMoneyDataProvider;
import com.zebrait.hibernate.SessionFactoryProvider;
import com.zebrait.hibernate.StockStatusEntryRepository;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.ContinuouGrowingStrategy;
import com.zebrait.strategy.NewStockStrategy;
import com.zebrait.strategy.Strategy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Get the candidate stocks stored in db, and check whether should buy or sell.
public class BuyAndSellProcessor {
    private static DataProvider dataProvider = new EastMoneyDataProvider();
    private static List<Strategy> strategies = Arrays.asList(new NewStockStrategy(),new ContinuouGrowingStrategy());
    private static StockStatusEntryRepository stockStatusEntryRepository = new StockStatusEntryRepository();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        for (Strategy strategy : strategies) {
            executorService.submit(() -> {
                List<StockStatusEntry> candidates = stockStatusEntryRepository.getStocksByStockStatusAndStrategy
                        (StockStatus.CANDIDATE, strategy.getClass());
                for (StockStatusEntry stockStatusEntry : candidates) {
                    if (strategy.checkBuy(dataProvider.getStockInfo(stockStatusEntry.getCode()))) {
                        stockStatusEntry.setStockStatus(StockStatus.BOUGHT);
                        stockStatusEntry.setLastUpdate(new Date());
                        stockStatusEntry.setPrice(0);
                        stockStatusEntryRepository.save(stockStatusEntry);
                    }
                }
            });
            executorService.submit(() -> {
                List<StockStatusEntry> boughts = stockStatusEntryRepository.getStocksByStockStatusAndStrategy
                        (StockStatus.CANDIDATE, strategy.getClass());
                for (StockStatusEntry stockStatusEntry : boughts) {
                    if (strategy.checkSell(dataProvider.getStockInfo(stockStatusEntry.getCode()))) {
                        stockStatusEntryRepository.freeStock(stockStatusEntry);
                    }
                }
            });
        }
        executorService.shutdown();
        SessionFactoryProvider.destroy();
    }
}
