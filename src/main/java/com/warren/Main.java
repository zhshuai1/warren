package com.warren;

import com.warren.model.repository.DynamicSessionFactoryProvider;
import com.warren.model.repository.SessionFactoryProvider;
import com.warren.model.repository.StockDayInfoRepository;
import com.warren.processor.Simulator;
import com.warren.strategy.Strategy;
import com.warren.strategy.continuousgrowing.ContinuousGrowingStrategy;
import com.warren.strategy.test.TestSimpleStrategy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Strategy> strategies = Arrays.asList(new ContinuousGrowingStrategy(),new TestSimpleStrategy());
        List<String> codes = new StockDayInfoRepository().getAllCodes();
        //List<String>codes=Arrays.asList("sh603666","sh601298","sh603039","sh603737","sh603986","sh600776");
        //List<String>codes=Arrays.asList("sh600419");
        System.out.println(codes);
        System.out.println(codes.size());
        Date start = new Date(118, 0, 1);
        Date end = new Date(119, 4, 1);
        Simulator.simulate(codes, strategies, start, end);
        SessionFactoryProvider.destroy();
        DynamicSessionFactoryProvider.destroy();
    }
}
