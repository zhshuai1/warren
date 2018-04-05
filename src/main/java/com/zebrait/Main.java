package com.zebrait;

import com.zebrait.hibernate.PriceRepository;
import com.zebrait.hibernate.SessionFactoryProvider;
import com.zebrait.model.Price;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Main {
    public static void main(String[] args) {
        PriceRepository priceRepository = new PriceRepository();
        Price price = Price.builder().code("sh600030").open(4.5f).close(4.5f).type(Price.Type.SH).build();
        priceRepository.save(price);
        SessionFactoryProvider.destroy();
    }
}
