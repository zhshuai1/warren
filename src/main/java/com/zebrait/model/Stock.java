package com.zebrait.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name="stock")
public class Stock {
    private String code;
    private String name;
    private String industryGroup;
    private String areaGroup;
    private List<String> conceptGroups;
}
