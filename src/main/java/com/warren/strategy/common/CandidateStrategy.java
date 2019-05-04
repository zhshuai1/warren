package com.warren.strategy.common;

import com.warren.model.StockDayInfo;

import java.util.List;

public interface CandidateStrategy {
    /**
     * check the whether it is a candidate before open; if it is a candidate, it could be bought today;
     * it could only use the data before today(not including)
     *
     * @param stockDayInfos
     * @param index
     * @return
     */
    boolean checkCandidate(List<StockDayInfo> stockDayInfos, int index);}
