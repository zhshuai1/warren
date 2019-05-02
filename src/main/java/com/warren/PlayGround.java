package com.warren;

import com.google.gson.Gson;
import com.warren.model.StockDayInfo;
import com.warren.model.repository.SessionFactoryProvider;
import com.warren.model.repository.StockDayInfoRepository;

import java.util.Date;
import java.util.List;

public class PlayGround {
    public static void main(String[] args) {
        Gson gson = new Gson();
        List<StockDayInfo> stockDayInfos = new StockDayInfoRepository().getStockDayInfos("sh000001");
        Date date0=stockDayInfos.get(0).getDate();
        System.out.println(""+date0.getYear()+date0.getMonth()+date0.getDate()+date0.getHours()+date0.getMinutes());
        System.out.println(gson.toJson(stockDayInfos.get(0).getDate()));
        System.out.println(gson.toJson(stockDayInfos.get(1).getDate()));
        System.out.println(gson.toJson(stockDayInfos.get(2).getDate()));
        Date date = new Date(119, 3, 30, 14, 0, 0);
        System.out.print(gson.toJson(date));
        System.out.println(gson.toJson(new StockDayInfoRepository().get(
                StockDayInfo.StockDayInfoKey.builder()
                        .code("sh000001")
                        .date(date)
                        .build())));
        SessionFactoryProvider.destroy();
    }
}
