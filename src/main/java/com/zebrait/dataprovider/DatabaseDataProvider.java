package com.zebrait.dataprovider;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DatabaseDataProvider extends DataProviderWithHistory {
    @Override
    public List<String> getAllStockCodes() {
        return null;
    }

    @Override
    public List<Price> getHistories(String code,int count) {
        return null;
    }

    @Override
    public Map<Date, Double> getTodayPrices(String code) {
        return null;
    }

    @Override
    public Stock getStockInfo(String code) {
        return null;
    }
}
