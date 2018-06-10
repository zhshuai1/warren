package com.zebrait.processor;

import com.zebrait.config.Configuration;
import com.zebrait.dataprovider.DataProvider;
import com.zebrait.dataprovider.EastMoneyDataProvider;
import com.zebrait.hibernate.StockStatusEntryRepository;
import com.zebrait.hibernate.StockTradingEntryRepository;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.model.StockTradingEntry;
import com.zebrait.strategy.Strategy;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class SellProcessor extends AbstractProcessor {
    private static DataProvider dataProvider = new EastMoneyDataProvider();
    private static List<Strategy> strategies = Configuration.STRATEGIES;
    private static StockStatusEntryRepository stockStatusEntryRepository = new StockStatusEntryRepository();
    private static StockTradingEntryRepository stockTradingEntryRepository = new StockTradingEntryRepository();

    @Override
    public void initialize() {
        objects = strategies;
    }

    @Override
    public void process(Object param) {
        Strategy strategy = (Strategy) param;
        log.info("Checking whether to buy for strategy: {}", strategy.getClass().getName());
        List<StockStatusEntry> candidates = stockStatusEntryRepository.getStocksByStockStatusAndStrategy(StockStatus
                .BOUGHT, strategy.getClass());
        for (StockStatusEntry stockStatusEntry : candidates) {
            String code = stockStatusEntry.getCode();
            if (strategy.checkSell(dataProvider.getStockInfo(code))) {
                StockTradingEntry stockTradingEntry = StockTradingEntry.builder().build();
                stockTradingEntryRepository.save(stockTradingEntry);
                stockStatusEntryRepository.freeStock(stockStatusEntry);
                log.info("Will sell {} for strategy {}", code, strategy.getClass().getName());
            } else {
                log.info("Will not sell {} for strategy {}", code, strategy.getClass().getName());
            }
        }

    }
}
