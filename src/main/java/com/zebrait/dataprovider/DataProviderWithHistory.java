package com.zebrait.dataprovider;

import com.zebrait.model.Price;
import com.zebrait.model.Stock;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DataProviderWithHistory implements DataProvider {
    public List<Price> getHistories(String code, int count, Date day) {
        return getHistories(code, count).stream().filter(price -> price.getDay().before(day)).collect(Collectors.toList());
    }

    public List<Price.MinuteData> getTodayPrices(String code, Date day) {
        return null;
    }

    public Stock getStockInfo(String code, Date day) {
        List<Price> histories = getHistories(code, 10000, day);
        List<Price.MinuteData> todayPrices = getTodayPrices(code, day);
        return Stock.builder().code(code).histories(histories).todayPrices(todayPrices).build();
    }
}
