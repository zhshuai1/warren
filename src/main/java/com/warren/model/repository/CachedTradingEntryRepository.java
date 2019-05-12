package com.warren.model.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.warren.model.TradingEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachedTradingEntryRepository extends TradingEntryRepository {

    private String formatKey(String code, String strategy) {
        return code + strategy;
    }

    private Cache<String, List<TradingEntry>> cache =
            CacheBuilder.newBuilder()
                    .initialCapacity(10)
                    .maximumSize(30)
                    .recordStats()
                    .concurrencyLevel(8)
                    .expireAfterAccess(2, TimeUnit.SECONDS).build();

    @Override
    public void save(TradingEntry entry) {
        super.save(entry);
        try {
            String key = formatKey(entry.getCode(), entry.getStrategy());
            List<TradingEntry> tradingEntries = cache.get(key, () -> new ArrayList<>());
            tradingEntries.add(0, entry);
            cache.put(key, tradingEntries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<TradingEntry> getTradingEntryByStrategyAndTime(Class<?> clazz, Date time) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TradingEntry> getTradingEntryByCodeAndStrategyOrderByDateDesc(String code, Class<?> clazz) {
        try {
            return cache.get(formatKey(code, clazz.getName()), () -> new ArrayList<>());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<TradingEntry> getLastTradingEntryByCodeAndStrategy(String code, Class<?> clazz) {
        //return super.getLastTradingEntryByCodeAndStrategy(code, clazz);
        throw new UnsupportedOperationException();
    }
}
