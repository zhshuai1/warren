package com.zebrait;

import com.zebrait.crawler.Crawler;
import com.zebrait.model.Stock;
import com.zebrait.processor.GetAllStocksProcessor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class Main {
    public static void main(String[] args) {
        /*PriceRepository priceRepository = new PriceRepository();
        Price price = Price.builder().code("sh600030").open(4.5f).close(4.5f).type(Price.Type.SH).build();
        priceRepository.save(price);
        SessionFactoryProvider.destroy();*/
       long start= System.currentTimeMillis();
        List<Stock> stocks = new GetAllStocksProcessor().getAllStocks();
        System.out.println(System.currentTimeMillis()-start);
        //System.out.println(stocks);
        stocks.stream().map(Stock::getName).forEach(System.out::println);
    }
}
