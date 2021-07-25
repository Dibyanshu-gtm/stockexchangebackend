package com.example.stockexchangebackend.controllers;

import com.example.stockexchangebackend.models.Company;
import com.example.stockexchangebackend.models.PriceResponse;
import com.example.stockexchangebackend.models.Sector;
import com.example.stockexchangebackend.models.StockExchange;
import com.example.stockexchangebackend.repositories.SectorRepository;
import com.example.stockexchangebackend.services.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class SectorController {

    @Autowired
    SectorRepository sectorRepository;
    @Autowired
    SectorService sectorService;
    @RequestMapping(value = "/sector", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String addSector(@RequestBody Sector sector){
        sectorRepository.save(sector);
        return "Done";
    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/getsectorpricedate/{name}",method = RequestMethod.GET)
    public ResponseEntity<List<PriceResponse>> getSectorPrice(@PathVariable String name, @RequestParam(name = "from")String from, @RequestParam(name = "todate")String todate, @RequestParam(name="exchangename")String exchangename) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        List<PriceResponse>resp= sectorService.getSectorStockPriceDate(name,dateFormat.parse(from),dateFormat.parse(todate),exchangename);
        if(resp.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<PriceResponse>>(resp,HttpStatus.OK);
    }

    @CrossOrigin(origins ={"http://127.0.0.1:3000","http://localhost:3000/","https://stockexchangefrontend.herokuapp.com"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/getsectorpriceyear/{name}")
    public ResponseEntity<List<PriceResponse>>getSectorPriceyear(@PathVariable String name,@RequestParam(name = "from")String from, @RequestParam(name = "todate")String todate,@RequestParam(name="exchangename")String exchangename) throws ParseException {
        DateFormat dateFormat= new SimpleDateFormat("yyyy");
        List<PriceResponse>resp = sectorService.getSectorStockPriceYear(name,dateFormat.parse(from),dateFormat.parse(todate),exchangename);
        if(resp.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<PriceResponse>>(resp,HttpStatus.OK);
    }
}
