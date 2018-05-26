package com.zebrait.dataprovider;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DataProvider {
    List<String> getAllStockCodes();

    // K line in day
    List<Price> getHistories(String code, int count);

    // Today prices for each minute
    Map<Date, Double> getTodayPrices(String code);

    // Stock metadata
    Stock getStockInfo(String code);
}
