package com.zebrait.dataprovider;

import com.google.gson.Gson;
import com.zebrait.crawler.Crawler;
import com.zebrait.model.Price;
import com.zebrait.model.Stock;
import com.zebrait.model.Type;
import com.zebrait.util.TransformUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class EastMoneyDataProvider implements DataProvider {
    private static String allStockUrl = "http://quote.eastmoney.com/stocklist.html";
    private static String indexesUrl = "http://quote.eastmoney.com/centerv2/hszs";
    private static String industryGroupUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS" +
            ".aspx?type=CT&cmd=C._BKHY&sty=FPGBKI&page=1&pageSize=50&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String conceptGroupUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS" +
            ".aspx?type=CT&cmd=C._BKGN&sty=FPGBKI&page=1&pageSize=500&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String areaGroupUrl = null;
    private static String industryGroupUrlTemplate = "";
    private static String conceptGroupUrlTemplate = "";
    private static String areaGroupUrlTemplate = null;
    private static String PATTERN = ".*http://quote\\.eastmoney\\.com/(\\w+)\\.html.*";

    @Override
    public List<String> getAllStockCodes() {
        return getStocksWithUrl(allStockUrl);
    }

    @Override
    public List<Price> getHistories(String code, int count) {
        String type = code.substring(0, 2);
        String pureCode = code.substring(2);
        return EastMoneyStockHistoryProvider.getPrices(pureCode, Type.valueOf(type), count);
    }

    @Override
    public Map<Date, Double> getTodayPrices(String code) {
        return null;
    }

    @Override
    public Stock getStockInfo(String code) {
        List<Price> histories = getHistories(code, 10000);
        Map<Date, Double> todayPrices = getTodayPrices(code);
        return Stock.builder().code(code).histories(histories).todayPrices(todayPrices).build();
    }

    private List<String> getStocksWithUrl(String url) {
        String content = Crawler.getContent(url, PATTERN);
        String[] entries = content.split("\\n");
        Pattern patten = Pattern.compile(PATTERN);
        return Arrays.stream(entries).map(e -> {
            Matcher matcher = patten.matcher(e);
            if (matcher.find())
                return matcher.group(1);
            else {
                return null;
            }
        }).distinct().collect(Collectors.toList());
    }
}

@Log4j2
class EastMoneyStockHistoryProvider {
    private static String shUrl = "http://pdfm.eastmoney" +
            ".com/EM_UBG_PDTI_Fast/api/js?rtntype=5&token=4f1862fc3b5e77c150a2b985b12db0fd&id=#####1&type=k" +
            "&authorityType=ba";
    private static String szUrl = "http://pdfm.eastmoney" +
            ".com/EM_UBG_PDTI_Fast/api/js?rtntype=5&token=4f1862fc3b5e77c150a2b985b12db0fd&id=#####2&type=k" +
            "&authorityType=ba";
    private static String groupUrl = "http://pdfm.eastmoney" +
            ".com/EM_UBG_PDTI_Fast/api/js?rtntype=5&token=4f1862fc3b5e77c150a2b985b12db0fd&id=#####&type=k" +
            "&authorityType=ba";
    private static Map<Type, String> mapping = new HashMap<>();

    static {
        mapping.put(Type.sh, shUrl);
        mapping.put(Type.sz, szUrl);
        mapping.put(Type.hy, groupUrl);
        mapping.put(Type.gn, groupUrl);
        mapping.put(Type.dq, groupUrl);
    }

    public static List<Price> getPrices(String code, Type type, int count) {
        String pureCode = code;
        if (code.startsWith("sh") || code.startsWith("sz")) {
            pureCode = code.substring(2);
        }
        String content = Crawler.getContent(mapping.get(type).replace("#####", pureCode)).trim();
        content = content.substring(1, content.length() - 1);
        StockData stockData = new Gson().fromJson(content, StockData.class);
        List<String> data = stockData.getData();
        if (data == null) {
            log.info("The data field of {} is null, maybe this is an invalid stock or type is wrong.", code);
            return Collections.emptyList();
        }
        List<Price> prices = new LinkedList<>();
        int begin = data.size() < count ? 0 : data.size() - count - 1;

        for (int i = begin; i < data.size(); ++i) {
            String[] fields = data.get(i).split(",");
            float open = Float.parseFloat(fields[1]);
            float close = Float.parseFloat(fields[2]);
            float yc = open;
            if (prices.size() > 0) {
                yc = prices.get(prices.size() - 1).getClose();
            }
            prices.add(Price.builder().code(code)
                    .day(TransformUtil.toDate(fields[0]))
                    .open(Float.parseFloat(fields[1]))
                    .close(close)
                    .high(Float.parseFloat(fields[3]))
                    .low(Float.parseFloat(fields[4]))
                    .volume(Long.parseLong(fields[5]))
                    .amount(TransformUtil.toNumberWithoutChinese(fields[6]))
                    .delta((close - yc) / yc)
                    .build());
        }
        if (data.size() > 0) {
            log.info("This is not a new stock {}, the data for the first element is not right, will remove it.", code);
            prices.remove(0);
        }
        return prices;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockData {
        private String name;
        private String code;
        private List<String> data;
        private List<Price> prices;
    }
}
