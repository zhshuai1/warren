package com.warren.processor;

import com.warren.model.StockDayInfo;
import com.warren.model.TradingEntry;
import com.warren.model.TradingEntry.TradingStatus;
import com.warren.model.TradingEntry.TradingType;
import com.warren.model.repository.StockDayInfoRepository;
import com.warren.model.repository.TradingEntryRepository;
import com.warren.strategy.Strategy;
import com.warren.strategy.model.TradingResult;
import com.warren.util.DateUtil;

import java.util.Date;
import java.util.List;

public class Simulator {
    private static final TradingEntryRepository tradingEntryRepository = new TradingEntryRepository();

    public static void simulate(List<String> codes, List<Strategy> strategies, Date start, Date end) {
        for (String code : codes) {
            List<StockDayInfo> stockDayInfos = new StockDayInfoRepository().getStockDayInfos(code);
            int startIndex = 0, endIndex = stockDayInfos.size() - 1;
            for (int i = 0; i < stockDayInfos.size(); ++i) {
                if (stockDayInfos.get(i).getDate().getTime() < start.getTime()) {
                    startIndex = i;
                }
                if (stockDayInfos.get(i).getDate().getTime() < end.getTime()) {
                    endIndex = i;
                }
            }
            for (Strategy strategy : strategies) {
                for (int i = startIndex; i < endIndex; ++i) {
                    checkCandidate(stockDayInfos, i, strategy);
                    checkBuy(stockDayInfos, i, strategy);
                    checkSell(stockDayInfos, i, strategy);
                }
            }
        }
    }

    private static TradingStatus getFormerStatus(List<TradingEntry> tradingEntriesOrderedByDateDesc) {
        TradingStatus formerStatus = TradingStatus.FREE;
        if (tradingEntriesOrderedByDateDesc.size() > 0) {
            TradingEntry lastTradingEntry = tradingEntriesOrderedByDateDesc.get(0);
            if (TradingType.CANDIDATE.equals(lastTradingEntry.getTradingType())) {
                formerStatus = TradingStatus.CANDIDATE;
            } else if (TradingType.BUY.equals(lastTradingEntry.getTradingType())) {
                formerStatus = TradingStatus.BOUGHT;
            }
        }
        return formerStatus;
    }

    private static void checkCandidate(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        String code = stockDayInfos.get(0).getCode();
        List<TradingEntry> tradingEntries = tradingEntryRepository.getTradingEntryByCodeAndStrategyOrderByDateDesc(code, strategy.getClass());
        TradingStatus formerStatus = getFormerStatus(tradingEntries);
        if (TradingStatus.BOUGHT.equals(formerStatus) || index <= 0) {
            return;
        }
        // for candidate checking, use data no later than yesterday
        // if i<=0; strategy should always return false
        boolean isCandidate = strategy.checkCandidate(stockDayInfos, index);
        StockDayInfo yesterdayInfo = stockDayInfos.get(index - 1);
        StockDayInfo todayInfo = stockDayInfos.get(index);
        if (isCandidate) {
            TradingEntry tradingEntry = TradingEntry.builder()
                    .code(todayInfo.getCode())
                    .date(DateUtil.format(todayInfo.getDate()))
                    .price(yesterdayInfo.getClose())
                    .strategy(strategy.getClass().getName())
                    .tradingType(TradingType.CANDIDATE)
                    .build();
            tradingEntryRepository.save(tradingEntry);
        } else {
            if (TradingStatus.CANDIDATE.equals(formerStatus)) {
                TradingEntry tradingEntry = TradingEntry.builder()
                        .code(todayInfo.getCode())
                        .date(DateUtil.format(todayInfo.getDate()))
                        .price(yesterdayInfo.getClose())
                        .strategy(strategy.getClass().getName())
                        .tradingType(TradingType.DECANDIDATE)
                        .build();
                tradingEntryRepository.save(tradingEntry);
            }
        }
    }

    private static void checkBuy(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        String code = stockDayInfos.get(0).getCode();
        List<TradingEntry> tradingEntries = tradingEntryRepository.getTradingEntryByCodeAndStrategyOrderByDateDesc(code, strategy.getClass());
        TradingStatus formerStatus = getFormerStatus(tradingEntries);
        if (!TradingStatus.CANDIDATE.equals(formerStatus)) {
            return;
        }
        TradingResult tradingResult = strategy.checkBuy(stockDayInfos, index, tradingEntries.get(0));
        if (tradingResult.isSuccess()) {
            StockDayInfo todayInfo = stockDayInfos.get(index);
            TradingEntry tradingEntry = TradingEntry.builder()
                    .code(todayInfo.getCode())
                    .date(DateUtil.format(tradingResult.getTime()))
                    .price(tradingResult.getPrice())
                    .strategy(strategy.getClass().getName())
                    .tradingType(TradingType.BUY)
                    .build();
            tradingEntryRepository.save(tradingEntry);
        }
    }

    private static void checkSell(List<StockDayInfo> stockDayInfos, int index, Strategy strategy) {
        String code = stockDayInfos.get(0).getCode();
        List<TradingEntry> tradingEntries = tradingEntryRepository.getTradingEntryByCodeAndStrategyOrderByDateDesc(code, strategy.getClass());
        TradingStatus formerStatus = getFormerStatus(tradingEntries);
        if (!TradingStatus.BOUGHT.equals(formerStatus)) {
            return;
        }
        TradingResult tradingResult = strategy.checkSell(stockDayInfos, index, tradingEntries.get(0));
        if (tradingResult.isSuccess()) {
            StockDayInfo todayInfo = stockDayInfos.get(index);
            TradingEntry tradingEntry = TradingEntry.builder()
                    .code(todayInfo.getCode())
                    .date(DateUtil.format(tradingResult.getTime()))
                    .price(tradingResult.getPrice())
                    .boughtPrice(tradingEntries.get(0).getPrice())
                    .strategy(strategy.getClass().getName())
                    .tradingType(TradingType.SELL)
                    .build();
            tradingEntryRepository.save(tradingEntry);
        }
    }
}