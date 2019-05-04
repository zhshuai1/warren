package com.warren.strategy;

import com.warren.strategy.common.BuyStrategy;
import com.warren.strategy.common.CandidateStrategy;
import com.warren.strategy.common.SellStrategy;

public interface Strategy extends CandidateStrategy, BuyStrategy, SellStrategy {
}
