package com.example.stockexchangebackend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="Sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable= false)
    private String sectorName;

    @Column (nullable = false)
    private String brief;

    @OneToMany(mappedBy ="sector")
    @JsonIgnore
    private List<Company> companies = new ArrayList<>();

    protected Sector(){

    }

    public Sector(String sectorName, String brief) {
        super();
        this.sectorName = sectorName;
        this.brief = brief;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
