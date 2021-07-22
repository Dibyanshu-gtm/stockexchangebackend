package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.PriceResponse;
import com.example.stockexchangebackend.models.Sector;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.repositories.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class SectorController {

    @Autowired
    SectorRepository sectorRepository;
    @RequestMapping(value = "/sector", method = RequestMethod.POST)
    public String addSector(@RequestBody Sector sector){
        sectorRepository.save(sector);
        return "Done";
    }
    @RequestMapping(value = "/sector/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Company>> getSector(@PathVariable long id){
        Sector sector = sectorRepository.findById(id).get();
        List<Company> companies = sector.getCompanies();
        if(companies.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Company>>(companies,HttpStatus.OK);
    }
    @RequestMapping(value = "/sector",method = RequestMethod.GET)
    public ResponseEntity<List<Sector>> getSectors()
    {
        List<Sector>sectorList= sectorRepository.findAll();
        if(sectorList.isEmpty())
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Sector>>(sectorList,HttpStatus.OK);
    }
    @RequestMapping(value = "/getsector/{id}",method = RequestMethod.GET)
    public ResponseEntity<?>getSectorId(@PathVariable Long id)
    {
        Sector sector= sectorRepository.findById(id).get();
        if(Objects.isNull(sector))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return  ResponseEntity.ok().body(sector);
    }

    @PutMapping(value = "/sector/{id}")
    public ResponseEntity<Sector> editSector(@RequestBody Sector sector)
    {
        Sector s = sectorRepository.findById(sector.getId()).get();
        s.setSectorName(sector.getSectorName());
        s.setBrief(sector.getBrief());
        Sector res = sectorRepository.save(s);
        if(Objects.isNull(res))
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(s);
    }

}
