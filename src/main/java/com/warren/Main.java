package com.warren;

import com.warren.processor.Simulator;
import com.warren.strategy.Strategy;
import com.warren.strategy.test.TestSimpleStrategy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Strategy> strategies = Arrays.asList(new TestSimpleStrategy());
        List<String> codes = Arrays.asList("sh600030");
        Date start = new Date(119, 0, 1);
        Date end = new Date(119, 4, 1);
        Simulator.simulate(codes, strategies, start, end);
    }
}