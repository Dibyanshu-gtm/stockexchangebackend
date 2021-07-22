package com.example.stockexchangebackend.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "IPODetail")
public class IPODetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double pricePerShare;

    @Column(nullable = false)
    private Long totalNumberOfShares;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Kolkata")
    private Date openDateTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Company company;

    //    @ManyToMany
//    private List<StockExchange> stockExchanges = new ArrayList<>();
    //@OneToMany(fetch = FetchType.LAZY,mappedBy = "ipoDetail")
    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "ipoDetail")
    @JsonIgnore
    private List<StockExchange> stockExchange;
    protected IPODetail(){

    }

    public IPODetail(Double pricePerShare, Long totalNumberOfShares, Date openDateTime) {
        this.pricePerShare = pricePerShare;
        this.totalNumberOfShares = totalNumberOfShares;
        this.openDateTime = openDateTime;
    }

    public Double getPricePerShare() {
        return pricePerShare;
    }

    public void setPricePerShare(Double pricePerShare) {
        this.pricePerShare = pricePerShare;
    }

    public Long getTotalNumberOfShares() {
        return totalNumberOfShares;
    }

    public void setTotalNumberOfShares(Long totalNumberOfShares) {
        this.totalNumberOfShares = totalNumberOfShares;
    }

    public Date getOpenDateTime() {
        return openDateTime;
    }

    public void setOpenDateTime(Date openDateTime) {
        this.openDateTime = openDateTime;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<StockExchange> getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(List<StockExchange> stockExchange) {
        this.stockExchange = stockExchange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
