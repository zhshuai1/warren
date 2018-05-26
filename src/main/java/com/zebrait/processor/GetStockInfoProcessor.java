package com.zebrait.processor;

import com.zebrait.model.StockGroupMapping;
import com.zebrait.model.Type;

import java.util.HashMap;
import java.util.Map;

public class GetStockInfoProcessor {
    private static Map<Type, String> mapping = new HashMap<>();

    static {
        String stockUrl1 = "http://nuff.eastmoney.com/EM_Finance2015TradeInterface/JS" +
                ".ashx?id=#####1&token=4f1862fc3b5e77c150a2b985b12db0fd";
        String stockUrl2 = "http://nuff.eastmoney.com/EM_Finance2015TradeInterface/JS" +
                ".ashx?id=#####2&token=4f1862fc3b5e77c150a2b985b12db0fd";
        String groupUrl = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS" +
                ".aspx?type=CT&cmd=#####&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
        mapping.put(Type.sh, stockUrl1);
        mapping.put(Type.sz, stockUrl2);
        mapping.put(Type.hy, groupUrl);
        mapping.put(Type.gn, groupUrl);
        mapping.put(Type.dq, groupUrl);

    }

    /**
     * This url is quite useful. This could be used to retrieve the real-time data.
     */
    public StockGroupMapping getStockInfo(String code, Type type) {
        throw new UnsupportedOperationException();
    }
}
