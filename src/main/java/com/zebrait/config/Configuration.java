package com.zebrait.config;

import com.zebrait.strategy.NewStockStrategy;
import com.zebrait.strategy.Strategy;
import com.zebrait.strategy.continuousgrowing.*;

import java.util.Arrays;
import java.util.List;

public class Configuration {
    public static List<Strategy> STRATEGIES = Arrays.asList(new NewStockStrategy(),
            new ContinuousGrowingStrategy_25_10_40(),
            new ContinuousGrowingStrategy_30_10_40(),
            new ContinuousGrowingStrategy_40_10_40(),
            new ContinuousGrowingStrategy_25_10_30(),
            new ContinuousGrowingStrategy_30_10_30(),
            new ContinuousGrowingStrategy_40_10_30());
}
