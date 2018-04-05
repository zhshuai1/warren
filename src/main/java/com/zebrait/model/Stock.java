package com.zebrait.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Stock {
    private String code;
    private String name;
    private String industryGroup;
    private String areaGroup;
    private List<String> conceptGroups;
}
