package com.zebrait.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "price")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private float open;
    private float close;
    private float high;
    private float low;
    private float delta;
    private long volume;
    private float turnover;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        SH, SZ, HYBK, GNBK, DQBK, ZS;
    }
}
