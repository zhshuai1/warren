package com.zebrait.strategy;

import com.zebrait.model.Stock;

/**
 * This is a transition name. The name should be changed after there is a unified name for simulation and real
 * decision system. For current case, add the prices (k line) up to yesterday; add the today prices one by one of
 * today. Say, when considering the case of 2018-10-11, prices will be up to 2018-10-10; the minute data on
 * 2018-10-11 will be used as today prices.
 */

public interface SimulationStrategy {
    /**
     * Check whether it is a candidate based on the passed k lines.
     *
     * @param stock
     * @return
     */
    CheckCandidateResult checkCandidate(Stock stock);

    /**
     * Check whether we should buy it based on the minute prices of that day. It may also need the past prices to
     * help make decision.
     *
     * @param stock
     * @return
     */
    CheckBuyResult checkBuy(Stock stock);

    /**
     * Check whether we should sell it based on the minute prices of that day. It may also need the past prices to
     * help make decision.
     *
     * @param stock
     * @return
     */
    CheckSellResult checkSell(Stock stock);
}
