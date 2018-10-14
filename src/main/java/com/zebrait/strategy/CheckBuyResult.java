package com.zebrait.strategy;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CheckBuyResult {
    private boolean shouldBuy;
    private double price;
    private int index;
}
