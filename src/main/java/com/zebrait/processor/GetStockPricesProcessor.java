package com.zebrait.processor;

import com.google.gson.Gson;
import com.zebrait.crawler.Crawler;
import com.zebrait.model.Price;
import com.zebrait.model.Type;
import com.zebrait.util.TransformUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

public class GetStockPricesProcessor {
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

    public List<Price> getPrices(String code, Type type, int count) {
        String pureCode = code;
        if (code.startsWith("sh") || code.startsWith("sz")) {
            pureCode = code.substring(2);
        }
        String content = Crawler.getContent(mapping.get(type).replace("#####", pureCode)).trim();
        content = content.substring(1, content.length() - 1);
        StockData stockData = new Gson().fromJson(content, StockData.class);
        List<String> data = stockData.getData();
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
        prices.remove(0);
        return prices;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockData {
        private String name;
        private String code;
        private Info info;
        private List<String> data;
        private List<Price> prices;
    }

    public static class Info {
        private float c;
        private float h;
        private float l;
        private float o;
        //amount, weighted volume sum with price
        private long a;
        //volume
        private long v;
        // yesterday close
        private float yc;
        private Date time;
        private String ticks;
    }
}
