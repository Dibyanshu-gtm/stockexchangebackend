package com.example.stockexchangebackend.repositories;

import com.example.stockexchangebackend.models.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector,Long> {
    Sector findBySectorName(String sector);
}
