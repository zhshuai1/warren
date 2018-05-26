package com.zebrait.model;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private Map<Date, Double> todayPrices;
}
