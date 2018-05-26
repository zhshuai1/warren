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
    private float open;
    private float close;
    private float high;
    private float low;
    private float delta;
    private long amount;
    private long volume;
    private float turnover;
    private Date day;
    @Enumerated(EnumType.STRING)
    private Type type;
}
