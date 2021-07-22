package com.example.stockexchangebackend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@NamedQuery(name="Company.findByName",query="SELECT c FROM Company c WHERE c.companyName= :companyName")
@Entity
@Table(name="Company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable=false)
    private Double turnover;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false)
    @Type(type="text")
    private String boardOfDirectors;

    @Column(nullable = false)
    @Type(type="text")
    private String companyBrief;

    @OneToOne(mappedBy = "company")
    @JsonIgnore
    private IPODetail ipo;

    @OneToMany(targetEntity = CompanyStockexchangemap.class)
    private List<CompanyStockexchangemap> compstockmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Sector sector;

    //addStockprice
    protected Company(){

    }

    public Company(String companyName, Double turnover, String ceo, String boardOfDirectors, String companyBrief) {
        super();
        this.companyName = companyName;
        this.turnover = turnover;
        this.ceo = ceo;
        this.boardOfDirectors = boardOfDirectors;
        this.companyBrief = companyBrief;
    }

    public IPODetail getIpo() {
        return ipo;
    }

    public void setIpo(IPODetail ipo) {
        this.ipo = ipo;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
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

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public String getCeo() {
        return ceo;
    }

    public void setCeo(String ceo) {
        this.ceo = ceo;
    }

    public String getBoardOfDirectors() {
        return boardOfDirectors;
    }

    public void setBoardOfDirectors(String boardOfDirectors) {
        this.boardOfDirectors = boardOfDirectors;
    }

    public String getCompanyBrief() {
        return companyBrief;
    }

    public void setCompanyBrief(String companyBrief) {
        this.companyBrief = companyBrief;
    }
}

