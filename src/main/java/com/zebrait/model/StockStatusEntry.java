package com.zebrait.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class StockStatusEntry implements Serializable {
    private String name;
    @Id
    @Column(length = 20)
    private String code;
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;
    @Id
    private String strategy;
    private Date lastUpdate;
    private double price;
}
