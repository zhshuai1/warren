package com.zebrait.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zebrait.crawler.Crawler;
import com.zebrait.model.StockGroupMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetAllStocksProcessor {
    private static String allStockUrl = "http://quote.eastmoney.com/stocklist.html";
    private static String indexesUrl="http://quote.eastmoney.com/centerv2/hszs";
    private static String industryGroupUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS" +
            ".aspx?type=CT&cmd=C._BKHY&sty=FPGBKI&page=1&pageSize=50&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String conceptGroupUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS" +
            ".aspx?type=CT&cmd=C._BKGN&sty=FPGBKI&page=1&pageSize=500&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String areaGroupUrl = null;
    private static String industryGroupUrlTemplate = "";
    private static String conceptGroupUrlTemplate = "";
    private static String areaGroupUrlTemplate = null;
    private static String PATTERN = ".*http://quote\\.eastmoney\\.com/(\\w+)\\.html.*";

    public List<StockGroupMapping> getAllStocks() {
        return getStocksWithUrl(allStockUrl);
    }
    public List<StockGroupMapping> getAllIndexes(){
        return getStocksWithUrl(indexesUrl);
    }

    public List<StockGroupMapping> getAllIndustryGroups() {
        String content = Crawler.getContent(industryGroupUrl).trim();
        String realContent = content.substring(1, content.length() - 1);
        List<String> groups = new Gson().fromJson(realContent, new TypeToken<ArrayList<String>>() {
        }.getType());
        return groups.stream().map(s -> StockGroupMapping.builder().code(s.split(",")[1]).build())
                .collect(Collectors.toList());

    }

    public List<StockGroupMapping> getAllConceptGroups() {
        String content = Crawler.getContent(conceptGroupUrl).trim();
        String realContent = content.substring(1, content.length() - 1);
        List<String> groups = new Gson().fromJson(realContent, new TypeToken<ArrayList<String>>() {
        }.getType());
        return groups.stream().map(s -> StockGroupMapping.builder().code(s.split(",")[1]).build())
                .collect(Collectors.toList());
    }

    public List<StockGroupMapping> getAllAreaGroups() {
        throw new UnsupportedOperationException();
    }

    public List<StockGroupMapping> getStocksInIndustryGroup(String code) {
        return getStocksWithUrl(industryGroupUrl);
    }

    public List<StockGroupMapping> getStocksInConceptGroup(String code) {
        return getStocksWithUrl(conceptGroupUrl);
    }

    public List<StockGroupMapping> getStocksInAreaGroup(String code) {
        throw new UnsupportedOperationException();
    }

    private List<StockGroupMapping> getStocksWithUrl(String url) {
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
        }).distinct().map(n -> StockGroupMapping.builder().code(n).build()).collect(Collectors.toList());
    }
}
