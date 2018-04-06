package com.zebrait.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private String industryGroup;
    private String areaGroup;
    private List<String> conceptGroups;
    @Enumerated(EnumType.STRING)
    private Type type;

    public enum Type {
        SH, SZ, HYBK, GNBK, DQBK, ZS;
    }
}
