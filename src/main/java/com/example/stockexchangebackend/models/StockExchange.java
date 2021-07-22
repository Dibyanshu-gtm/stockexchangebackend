package com.example.stockexchangebackend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="StockExchange")
public class StockExchange {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(targetEntity = CompanyStockexchangemap.class)
    @JsonIgnore
    private List<CompanyStockexchangemap> compstockmap;

    //@ManyToOne(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<IPODetail> ipoDetail;

    public List<IPODetail> getIpoDetail() {
        return ipoDetail;
    }

    public void setIpoDetail(List<IPODetail> ipoDetail) {
        this.ipoDetail = ipoDetail;
    }

    public long getId() {
        return id;
    }
    public StockExchange(){
        super();
    }
    public StockExchange(String name) {
        super();
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompanyStockexchangemap> getCompstockmap() {
        return compstockmap;
    }

    public void setCompstockmap(List<CompanyStockexchangemap> compstockmap) {
        this.compstockmap = compstockmap;
    }
}