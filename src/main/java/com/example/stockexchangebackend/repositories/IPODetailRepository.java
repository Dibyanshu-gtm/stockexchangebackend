package com.example.stockexchangebackend.repositories;


import com.example.stockexchangebackend.models.IPODetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IPODetailRepository extends JpaRepository<IPODetail,Long> {

    @Query("SELECT I from IPODetail I WHERE I.company.companyName=:companyName")
    IPODetail findByCompanyName(String companyName);
    @Query("SELECT I FROM IPODetail I WHERE I.id=:id")
    Optional<IPODetail> findById(Long id);
}
