package com.example.stockexchangebackend.repositories;


import com.example.stockexchangebackend.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company  findByName(String companyName);
    @Query("SELECT C FROM Company C WHERE C.id=:id")
    Company findByCompanyId(Long id);
}
