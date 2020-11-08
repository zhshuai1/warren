package com.warren.util.series;

import com.warren.model.StockDayInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeriesUtil {
    public static double maxFall(List<StockDayInfo> stockDayInfos, int start, int end) {
        List<Double> maxFromLeft = new ArrayList<>(end - start + 1);
        double max = stockDayInfos.get(start).getAuthority();
        for (int i = start; i <= end; ++i) {
            if (max < stockDayInfos.get(i).getAuthority()) {
                max = stockDayInfos.get(i).getAuthority();
            }
            maxFromLeft.add(max);
        }
        double maxFall = 0;
        for (int i = end; i >= start; --i) {
            double currentFall = stockDayInfos.get(i).getAuthority() / maxFromLeft.get(i - start) - 1;
            if (maxFall > currentFall) {
                maxFall = currentFall;
            }
        }
        return maxFall;
    }

    public static double maxRise(List<StockDayInfo> stockDayInfos, int start, int end) {
        return 0;
    }

    public static boolean isUpperBoundary(List<StockDayInfo> stockDayInfos, int index) {
        throw new UnsupportedOperationException();
    }

    public static boolean isLowerBoundary(List<StockDayInfo> stockDayInfos, int index) {
        throw new UnsupportedOperationException();
    }

    public static List<Double> mv(List<StockDayInfo> stockDayInfos, int n) {
        if (stockDayInfos.size() < n) {
            return Collections.EMPTY_LIST;
        }
        List<Double> avgs = new ArrayList<>(stockDayInfos.size());
        double tmpAvg = 0;
        for (int i = 0; i < n; ++i) {
            tmpAvg += stockDayInfos.get(i).getClose();
        }
        tmpAvg /= n;
        avgs.add(tmpAvg);
        for (int i = n; i < stockDayInfos.size(); ++i) {
            tmpAvg += (stockDayInfos.get(i).getClose() - stockDayInfos.get(i - n).getClose()) / n;
            avgs.add(tmpAvg);
        }
        return avgs;
    }
}
