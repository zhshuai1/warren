package com.warren.util.series;

import com.warren.model.StockDayInfo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SeriesUtilTest {

    @Test
    public void maxFall() {
    }

    @Test
    public void maxRise() {
    }

    @Test
    public void isUpperBoundary() {
    }

    @Test
    public void isLowerBoundary() {
    }

    @Test
    public void mvDataLessThanN() {
        List<StockDayInfo> stockDayInfos = new ArrayList<>(10);
        for (int i = 0; i < 3; ++i) {
            stockDayInfos.add(StockDayInfo.builder().close((double) i).build());
        }
        List<Double> avgs = SeriesUtil.mv(stockDayInfos, 5);
        assertEquals(0, avgs.size());
    }

    @Test
    public void mvDataLessEqualsN() {
        List<StockDayInfo> stockDayInfos = new ArrayList<>(10);
        for (int i = 0; i < 5; ++i) {
            stockDayInfos.add(StockDayInfo.builder().close((double) i).build());
        }
        List<Double> avgs = SeriesUtil.mv(stockDayInfos, 5);
        assertEquals(1, avgs.size());
        assertEquals(2.0, avgs.get(0), 1e-10);
    }

    @Test
    public void mvDataMoreThanN() {
        List<StockDayInfo> stockDayInfos = new ArrayList<>(10);
        double[] values = new double[]{3.5, 4.5, 6.5, 7.5, 2.5, 9.5, 4.5, 5.5};
        for (int i = 0; i < 8; ++i) {
            stockDayInfos.add(StockDayInfo.builder().close(values[i]).build());
        }
        List<Double> avgs = SeriesUtil.mv(stockDayInfos, 5);
        assertEquals(4, avgs.size());
        assertEquals(4.9, avgs.get(0), 1e-10);
        assertEquals(6.1, avgs.get(1), 1e-10);
        assertEquals(6.1, avgs.get(2), 1e-10);
        assertEquals(5.9, avgs.get(3), 1e-10);
    }
}