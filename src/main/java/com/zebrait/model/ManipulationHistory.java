package com.zebrait.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class ManipulationHistory {
    private String code;
    private Date date;
    private String strategy;
    private boolean buy;
    private double profit;
}
