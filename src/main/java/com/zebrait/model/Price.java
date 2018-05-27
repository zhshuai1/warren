package com.zebrait.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private String name;
    private String code;
    private double open;
    private double close;
    private double high;
    private double low;
    private double delta;
    private long amount;
    private long volume;
    private double turnover;
    private Date day;
    @Enumerated(EnumType.STRING)
    private Type type;
}
