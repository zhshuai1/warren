package com.zebrait.processor;

import com.zebrait.crawler.Crawler;
import com.zebrait.model.Stock;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetAllStocksProcessor {
    private static String allStockUrl = "http://quote.eastmoney.com/stocklist.html";
    private static String PATTERN = ".*http://quote\\.eastmoney\\.com/(\\w+)\\.html.*";

    public List<Stock> getAllStocks() {
        String content = Crawler.getContent(allStockUrl, PATTERN);
        String[] entries = content.split("\\n");
        Pattern patten = Pattern.compile(PATTERN);
        return Arrays.stream(entries).map(e -> {
            Matcher matcher = patten.matcher(e);
            if (matcher.find())
                return matcher.group(1);
            else {
                return null;
            }
        }).distinct().map(n -> Stock.builder().name(n).build()).collect(Collectors.toList());
    }
}
