package com.zebrait.simulation;

import com.zebrait.model.Stock;
import com.zebrait.strategy.SimulationStrategy;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.LinkedList;
import java.util.List;

public class Simulation {
    public static void main(String[] args) {
        List<Stock> stocks = new LinkedList<>();
        List<SimulationStrategy> strategies = new LinkedList<>();
        stocks.stream().flatMap(stock -> strategies.stream().map(strategy -> ImmutablePair.of(stock, strategy))).forEach
                (pair -> Simulator.simulateForOneStock(pair.left, pair.right));
    }
}
