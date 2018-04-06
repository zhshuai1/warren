package com.zebrait.processor;

import com.zebrait.crawler.Crawler;
import com.zebrait.model.Price;

import java.util.List;

public class GetStockPricesProcessor {
    public List<Price> getPrices(String code) {
        String content = Crawler.getContent("");
        return null;
    }
}
