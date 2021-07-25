package com.example.stockexchangebackend.repositories;



import com.example.stockexchangebackend.models.CompanyStockexchangemap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyStockexchangemapRepository extends JpaRepository<CompanyStockexchangemap,Long> {

    @Query("SELECT C from CompanyStockexchangemap C WHERE C.CompanyCode= :CompanyCode AND C.stockExchange.name= :name")
    CompanyStockexchangemap findByCompanyCodeAndStockExchange(String CompanyCode,String name);
    @Query("SELECT C from CompanyStockexchangemap  C WHERE C.company.companyName=:companyName")
    CompanyStockexchangemap findByCompany(String companyName);
    @Query("SELECT C from CompanyStockexchangemap C WHERE C.company.companyName=:companyName AND C.stockExchange.name= :name")
    CompanyStockexchangemap findByCompanyNameAndStockExchange(String companyName,String name);
}
