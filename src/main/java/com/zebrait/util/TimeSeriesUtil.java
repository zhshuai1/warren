package com.zebrait.util;

import com.zebrait.model.Price;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class TimeSeriesUtil {
    public static double maxFall(List<Price> prices, int start, int end) {
        if (start < 0) {
            start = prices.size() + start;
        }
        if (end <= 0) {
            end = prices.size() + end;
        }
        List<Double> closes = prices.stream().map(Price::getClose).collect(Collectors.toList());
        double[] mins = new double[closes.size()];
        double min = Double.MAX_VALUE;
        for (int i = end; i >= start; --i) {
            min = closes.get(i) < min ? closes.get(i) : min;
            mins[i] = min;
        }
        double[] maxes = new double[closes.size()];
        double max = Double.MIN_VALUE;
        for (int i = start; i <= end; ++i) {
            max = closes.get(i) > max ? closes.get(i) : max;
            maxes[i] = max;
        }
        return IntStream.range(0, closes.size()).boxed().map(i ->  (mins[i] - maxes[i]) / maxes[i]).reduce
                (Double.MAX_VALUE, (x, y) -> x < y ? x : y);
    }

    public static double maxFall(List<Price> prices) {
        return maxFall(prices, 0, prices.size() - 1);
    }

    public static List<Double> avgLine(List<Price> prices, int windowSize) {
        List<Double> closes = prices.stream().map(p -> p.getClose()).collect(Collectors.toList());
        List<Double> avgs = new LinkedList<>();
        if (closes.size() < windowSize) {
            return avgs;
        }
        double accumulation =
                IntStream.range(0, windowSize - 1).boxed().map(i -> closes.get(i)).reduce(0.0, (x, y) -> x + y);
        for (int i = windowSize - 1; i < closes.size(); ++i) {
            accumulation += closes.get(i);
            avgs.add(accumulation / windowSize);
            accumulation -= closes.get(i - windowSize + 1);
        }
        return avgs;
    }

    public static double totalIncrease(List<Price> prices) {
        return 0;
    }
}
