package com.example.stockexchangebackend.models;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "StockPrice")
public class StockPrice {

    @Id
    @GeneratedValue
    private Long id;
    //    @Column(nullable = false)
//    private String exchangename;
    @Column(nullable = false)
    private String CompanyCode;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Kolkata")
    private Date localDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    private StockExchange stockExchange;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;

    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private Date time;
    private float shareprice;

    public StockPrice(){

    }
    public StockPrice( String CompanyCode, Date date, Date time, float shareprice,Date localDateTime) {
        super();
        this.CompanyCode = CompanyCode;
        this.date = date;
        this.time = time;
        this.shareprice = shareprice;
        this.localDateTime=localDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getCompanycode() {
        return CompanyCode;
    }

    public void setCompanycode(String CompanyCode) {
        this.CompanyCode = CompanyCode;
    }

    public Date getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(Date localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    public float getShareprice() {
        return shareprice;
    }

    public void setShareprice(float shareprice) {
        this.shareprice = shareprice;
    }
}

