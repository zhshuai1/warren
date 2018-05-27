package com.zebrait;

import com.zebrait.hibernate.StockStatusEntryRepository;
import com.zebrait.model.StockStatus;
import com.zebrait.model.StockStatusEntry;
import com.zebrait.strategy.NewStockStrategy;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@Log4j2
public class Main {
    public static void main(String[] args) {
        /*PriceRepository priceRepository = new PriceRepository();
        Price price = Price.builder().code("sh600030").open(4.5f).close(4.5f).type(Price.Type.sh).build();
        priceRepository.save(price);
        SessionFactoryProvider.destroy();*/
        //long start = System.currentTimeMillis();
        //List<StockGroupMapping> stocks = new GetAllStocksProcessor().getAllConceptGroups();
        //List<Price> prices = new GetStockPricesProcessor().getPrices("BK04571", Type.hy, 50);
        //System.out.println(System.currentTimeMillis() - start);
        //System.out.println(stocks);
        //stocks.stream().map(StockGroupMapping::getName).forEach(System.out::println);

        StockStatusEntryRepository stockStatusEntryRepository = new StockStatusEntryRepository();
        StockStatusEntry stockStatusEntry = StockStatusEntry.builder().code("sh600031").name("不知火舞").stockStatus
                (StockStatus.FREE).strategy(NewStockStrategy.class.getName())
                .build();
        stockStatusEntryRepository.saveOrUpdate(stockStatusEntry);
        List<StockStatusEntry> entries = stockStatusEntryRepository.getStocksByStockStatusAndStrategy(StockStatus.FREE,
                NewStockStrategy.class);
        stockStatusEntryRepository.freeStock(stockStatusEntry);
        return;
    }
}
