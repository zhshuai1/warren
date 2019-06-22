package com.warren.processor;

import com.warren.model.RunningTradingEntry;
import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.model.repository.RunningTradingEntryRepository;
import com.warren.model.repository.StockDayInfoRepository;
import com.warren.strategy.Strategy;
import com.warren.util.DateUtil;

import java.util.List;

public class Runner {
    private static final RunningTradingEntryRepository tradingEntryRepository = new RunningTradingEntryRepository();
    private static final StockDayInfoRepository stockDayInfoRepository = new StockDayInfoRepository();

    public static void checkCandidate(List<String> codes, List<Strategy> strategies) {
        for (String code : codes) {
            List<StockDayInfo> stockDayInfos = stockDayInfoRepository.getStockDayInfos(code);
            for (Strategy strategy : strategies) {
                long startTime = System.currentTimeMillis();
                System.out.println(String.format("Checking for %s, %d records, strategy: %s",
                        code, stockDayInfos.size(), strategy.getClass().getName()));
                checkCandidate(stockDayInfos, stockDayInfos.size(), strategy);
                System.out.println("Time cost: " + (System.currentTimeMillis() - startTime));
            }
        }
    }

    private static RunningTradingEntry.TradingStatus getFormerStatus(List<RunningTradingEntry> tradingEntriesOrderedByDateDesc) {
        RunningTradingEntry.TradingStatus formerStatus = RunningTradingEntry.TradingStatus.FREE;
        if (tradingEntriesOrderedByDateDesc.size() > 0) {
            RunningTradingEntry lastTradingEntry = tradingEntriesOrderedByDateDesc.get(0);
            if (RunningTradingEntry.TradingType.CANDIDATE.equals(lastTradingEntry.getTradingType())) {
                formerStatus = RunningTradingEntry.TradingStatus.CANDIDATE;
            } else if (TradingEntry.TradingType.BUY.equals(lastTradingEntry.getTradingType())) {
                formerStatus = RunningTradingEntry.TradingStatus.BOUGHT;
            }
        }
        return formerStatus;
    }

    private static void checkCandidate(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        try {
            String code = stockDayInfos.get(0).getCode();
            List<RunningTradingEntry> tradingEntries = tradingEntryRepository.getTradingEntryByCodeAndStrategyOrderByDateDesc(code, strategy.getClass());
            RunningTradingEntry.TradingStatus formerStatus = getFormerStatus(tradingEntries);
            if (RunningTradingEntry.TradingStatus.BOUGHT.equals(formerStatus) || index <= 0) {
                return;
            }
            // for candidate checking, use data no later than yesterday
            // if i<=0; strategy should always return false
            boolean isCandidate = strategy.checkCandidate(stockDayInfos, index);
            StockDayInfo yesterdayInfo = stockDayInfos.get(index - 1);
            if (isCandidate) {
                RunningTradingEntry tradingEntry = RunningTradingEntry.builder()
                        .code(yesterdayInfo.getCode())
                        .date(DateUtil.format(yesterdayInfo.getDate()))
                        .price(yesterdayInfo.getClose())
                        .strategy(strategy.getClass().getName())
                        .tradingType(RunningTradingEntry.TradingType.CANDIDATE)
                        .build();
                tradingEntryRepository.save(tradingEntry);
            } else {
                if (RunningTradingEntry.TradingStatus.CANDIDATE.equals(formerStatus)) {
                    RunningTradingEntry tradingEntry = RunningTradingEntry.builder()
                            .code(yesterdayInfo.getCode())
                            .date(DateUtil.format(yesterdayInfo.getDate()))
                            .price(yesterdayInfo.getClose())
                            .strategy(strategy.getClass().getName())
                            .tradingType(RunningTradingEntry.TradingType.DECANDIDATE)
                            .build();
                    tradingEntryRepository.save(tradingEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkBuy(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        try {
            // TODO: Implement this method later;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkSell(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        try {
            // TODO: Implement this method later;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
