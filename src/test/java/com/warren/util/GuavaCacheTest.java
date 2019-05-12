package com.warren.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.warren.model.TradingEntry;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GuavaCacheTest {
    @Test
    public void test()throws Exception{
        Cache<String, List<TradingEntry>> cache =
                CacheBuilder.newBuilder()
                        .initialCapacity(10)
                        .maximumSize(3)
                        .recordStats()
                        .concurrencyLevel(8)
                        .expireAfterAccess(2, TimeUnit.SECONDS).build();
        System.out.println(cache.get("Hello1",()-> Arrays.asList(null,null)));
        System.out.println(cache.get("Hello2",()-> Arrays.asList(null,null)));
        System.out.println(cache.get("Hello3",()-> Arrays.asList(null,null)));
        //Thread.sleep(3000);
        System.out.println(cache.getIfPresent("Hello1"));

        System.out.println(cache.get("Hello4",()-> Arrays.asList(null,null)));

        System.out.println(cache.get("Hello5",()-> Arrays.asList(null,null)));
        System.out.println(cache.getIfPresent("Hello2"));
    }
}
