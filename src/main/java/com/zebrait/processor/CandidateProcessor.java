package com.zebrait.processor;

import com.google.gson.Gson;
import com.zebrait.dataprovider.DataProvider;
import com.zebrait.dataprovider.EastMoneyDataProvider;
import com.zebrait.hibernate.SessionFactoryProvider;
import com.zebrait.hibernate.StockStatusEntryRepository;
import com.zebrait.model.Stock;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.ContinuouGrowingStrategy;
import com.zebrait.strategy.NewStockStrategy;
import com.zebrait.strategy.Strategy;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

@Log4j2
// Check all stocks and filter the ones comply with the strategy and save to db.
// This could be done when buy and sell. But considering the performance,  I separate this as a single task. With
// this filter, the number of candidate will be much smaller.
public class CandidateProcessor {
    private static final DataProvider dataProvider = new EastMoneyDataProvider();
    private static final List<Strategy> strategies = Arrays.asList(new NewStockStrategy(), new ContinuouGrowingStrategy());
    private static final StockStatusEntryRepository stockStatusEntryRepository = new StockStatusEntryRepository();
    private static final Gson GSON = new Gson();


    // TODO: Also add a history entry when stock status changes.
    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = Executors.newFixedThreadPool(50);

        List<String> stockCodes = dataProvider.getAllStockCodes();
        //List<String> stockCodes = Arrays.asList("sh600349");
        Map<String, Integer> failed = new HashMap<>();
        Queue<Pair<String, Future>> resultFutures = new LinkedList<>();
        for (String code : stockCodes) {
            Future result = executorService.submit(() -> consumer.accept(code));
            resultFutures.add(ImmutablePair.of(code, result));
        }
        while (resultFutures.size() > 0) {
            Pair<String, Future> result = resultFutures.poll();
            String code = result.getLeft();
            Future future = result.getRight();
            try {
                future.get();
            } catch (Exception e) {
                failed.putIfAbsent(code, 1);
                int count = failed.get(code);
                failed.put(code, count + 1);
                if (failed.get(code) <= 5) {
                    log.warn("Error occurred when handling stock {} for {} times, will have another try...", code,
                            count, e);
                    resultFutures.offer(ImmutablePair.of(code, executorService.submit(() -> consumer.accept(code))));
                } else {
                    log.error("Failed 5 times for the stock {}, will no longer try.", code);
                }

            }
        }
        executorService.shutdown();
        SessionFactoryProvider.destroy();
    }

    private static Consumer<String> consumer = code -> {
        Stock stock = dataProvider.getStockInfo(code);
        for (Strategy strategy : strategies) {
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
                            .CANDIDATE).code(stock.getCode()).name(stock.getName()).lastUpdate(new Date())
                            .price(0).strategy(strategy.getClass().getName()).build();
                    log.info("The stock {} did not in our sight recently.", code);
                } else {
                    stockStatusEntry = stockStatusEntries.get(0);
                    stockStatusEntry.setLastUpdate(new Date());
                    stockStatusEntry.setPrice(0);
                    log.info("The stock {} has already been a candidate, and will update it.", code);
                }
                stockStatusEntryRepository.save(stockStatusEntry);
            } else {
                log.info("The stock {} is not a candidate", code);
                if (!stockStatusEntries.isEmpty()) {
                    stockStatusEntryRepository.freeStock(stockStatusEntries.get(0));
                }
            }
        }
    };

}
