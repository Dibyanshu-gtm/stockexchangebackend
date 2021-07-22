package com.example.stockexchangebackend.models;

import java.util.Date;

public class IPOResponse {

    private Long id;
    private String companyName;
    private Double pricePerShare;
    private Long totalNumberOfShares;
    private String openDateTime;
    private String exchange;

    public IPOResponse(){

    }
    public IPOResponse(Long id, String companyName, Double pricePerShare, Long totalNumberOfShares, String openDateTime, String exchange) {
        this.id = id;
        this.companyName = companyName;
        this.pricePerShare = pricePerShare;
        this.totalNumberOfShares = totalNumberOfShares;
        this.openDateTime = openDateTime;
        this.exchange = exchange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getOpenDateTime() {
        return openDateTime;
    }

    public void setOpenDateTime(String openDateTime) {
        this.openDateTime = openDateTime;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}