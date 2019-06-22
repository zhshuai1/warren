package com.warren.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(StockDayInfo.StockDayInfoKey.class)
@Table(name="stock")
public class StockDayInfo {
    @Id
    private String code;
    private String type;
    @Id
    private Date date;
    private double open;
    private double close;
    private double high;
    private double low;
    private double delta;
    private Double authority;
    private long volume;
    @Type(type="text")
    private String minute;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class StockDayInfoKey implements Serializable {
        private String code;
        private Date date;
    }
}
