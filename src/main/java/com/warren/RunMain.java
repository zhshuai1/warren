package com.warren;

import com.warren.model.repository.DynamicSessionFactoryProvider;
import com.warren.model.repository.SessionFactoryProvider;
import com.warren.model.repository.StockDayInfoRepository;
import com.warren.processor.Runner;
import com.warren.processor.Simulator;
import com.warren.strategy.Strategy;
import com.warren.strategy.continuousgrowing.ContinuousGrowingStrategy;
import com.warren.strategy.test.TestSimpleStrategy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RunMain {
    public static void main(String[] args) {
        List<Strategy> strategies = Arrays.asList(new ContinuousGrowingStrategy(),new TestSimpleStrategy());
        List<String> codes = new StockDayInfoRepository().getAllCodes();
        System.out.println(codes.size());
        System.out.println(codes);
        Runner.checkCandidate(codes, strategies);
        SessionFactoryProvider.destroy();
        DynamicSessionFactoryProvider.destroy();
    }
}
