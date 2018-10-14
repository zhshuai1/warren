package com.zebrait.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private long id;
    private String code;
    private String name;
    private String industryGroup;
    private String areaGroup;
    private List<String> conceptGroups;
    private Type type;
    private List<Price> histories;
    // 1 minute prices
    private List<Price.MinuteData> todayPrices;
}
