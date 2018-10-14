package com.zebrait.strategy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckSellResult {
    private boolean shouldSell;
    private double price;
    private int index;
}
