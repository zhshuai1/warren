package com.zebrait.processor;

import com.google.gson.Gson;
import com.zebrait.config.Configuration;
import com.zebrait.dataprovider.DataProviderWithHistory;
import com.zebrait.dataprovider.EastMoneyDataProvider;
import com.zebrait.hibernate.SessionFactoryProvider;
import com.zebrait.hibernate.StockStatusEntryRepository;
import com.zebrait.model.Stock;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.Strategy;
import lombok.extern.log4j.Log4j2;

import java.util.Date;
import java.util.List;

@Log4j2
// Check all stocks and filter the ones comply with the strategy and save to db.
// This could be done when buy and sell. But considering the performance,  I separate this as a single task. With
// this filter, the number of candidate will be much smaller.
public class CandidateProcessor extends AbstractProcessor {
    private static final DataProviderWithHistory dataProvider = new EastMoneyDataProvider();
    private static final List<Strategy> strategies = Configuration.STRATEGIES;
    private static final StockStatusEntryRepository stockStatusEntryRepository = new StockStatusEntryRepository();
    private static final Gson GSON = new Gson();


    // TODO: Also add a history entry when stock status changes.
    public static void main(String[] args) throws Exception {
        try {
            new CandidateProcessor().run();
        } finally {
            SessionFactoryProvider.destroy();
        }
    }


    @Override
    public void initialize() {
        super.objects = dataProvider.getAllStockCodes();
    }

    @Override
    public void process(Object param) {
        String code = (String) param;

        String pureCode = code.substring(2);
        if (pureCode.startsWith("5") || pureCode.startsWith("1") || pureCode.startsWith("2")) {
            log.info("{} should be a fund, not a stock, will skip...", code);
            return;
        }
        Date date = new Date(new Date().getTime());
        Stock stock = dataProvider.getStockInfo(code, date);
        for (Strategy strategy : strategies) {
            try {
                log.info("Checking stock {} for strategy {}", code, strategy.getClass().getName());
                List<StockStatusEntry> stockStatusEntries = stockStatusEntryRepository.getStocksByCodeAndStrategy
                        (stock.getCode(), strategy.getClass());
                if (stockStatusEntries.size() > 1) {
                    log.warn("StockStatusEntries size is larger than 1: {}", GSON.toJson(stockStatusEntries));
                }
                if (stockStatusEntries.size() >= 1) {
                    if (StockStatus.BOUGHT.equals(stockStatusEntries.get(0).getStockStatus())) {
                        log.info("You have bought this stock {}, will not check it.", code);
                        continue;
                    }
                }
                if (strategy.checkCandidate(stock)) {
                    StockStatusEntry stockStatusEntry;
                    if (stockStatusEntries.isEmpty()) {
                        stockStatusEntry = StockStatusEntry.builder().stockStatus(StockStatus
                                .CANDIDATE).code(stock.getCode()).name(stock.getName()).lastUpdate(date)
                                .price(0).strategy(strategy.getClass().getName()).build();
                        log.info("The stock {} did not in our sight recently.", code);
                    } else {
                        stockStatusEntry = stockStatusEntries.get(0);
                        stockStatusEntry.setLastUpdate(new Date());
                        stockStatusEntry.setPrice(0);
                        log.info("The stock {} has already been a candidate, and will update it.", code);
                    }
                    stockStatusEntryRepository.saveOrUpdate(stockStatusEntry);
                } else {
                    log.info("The stock {} is not a candidate", code);
                    if (!stockStatusEntries.isEmpty()) {
                        stockStatusEntryRepository.freeStock(stockStatusEntries.get(0));
                    }
                }
            } catch (Exception e) {
                log.error("Error occurred when handling code:strategy {}:{}", code, strategy, e);
            }
        }
    }
}
