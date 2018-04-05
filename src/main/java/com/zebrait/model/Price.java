package com.zebrait.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Price {
    private String code;
    private float open;
    private float close;
    private float high;
    private float low;
    private float delta;
    private long volume;
    private float turnover;
    private Type type;

    public enum Type {
        SH, SZ, HYBK, GNBK, DQBK, ZS;
    }
}
