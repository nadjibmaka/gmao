package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.EditStationForm;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.Equipement;
import com.naftal.gmao.model.Station;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.EquipementRepository;
import com.naftal.gmao.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class EquipementAPIs {


    @Autowired
    EquipementRepository equipementRepository;
    @Autowired
    StationRepository stationRepository;


    @GetMapping("/equipements")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Equipement> getEquipements() {
        System.out.println("get equipements called");

        List<Equipement> list = equipementRepository.findByExiste(true);
        return list;
    }



    @GetMapping("/equipements/{NS}")
    @PreAuthorize("hasRole('ADMIN')")
    public Equipement getEquipement(@PathVariable String NS) {
        System.out.println("get equipement called");

       Equipement equipement = equipementRepository.findByEquipementNS(NS);
        return equipement;
    }



    @PostMapping("/saveEquipement/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveEquipement(@Valid @RequestBody Equipement equipement, @PathVariable String codeStation) {
        System.out.println("save equipement called");

        Station station=stationRepository.findByCodeStation(codeStation);
        equipement.setStation(station);
        for (Composant comp:equipement.getComposants()) {
            comp.setEquipement(equipement);
        }

        equipementRepository.save(equipement);
        return new ResponseEntity<>(new ResponseMessage("Equipement registred successfully!"), HttpStatus.OK);
    }

    @PutMapping("/equipements/{NS}/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateEquipement(@Valid @RequestBody Equipement equipement , @PathVariable String NS, @PathVariable String codeStation) {



        System.out.println("put equipement called");
        Equipement equipement2= equipementRepository.findByEquipementNS(NS);
        for (Composant comp:equipement2.getComposants()) {
            comp.setEquipement(null);
        }

        Station station=stationRepository.findByCodeStation(codeStation);
        equipement.setStation(station);

        Equipement equipement1= equipementRepository.findByEquipementNS(NS);

        equipement1=equipement;
        for (Composant comp:equipement1.getComposants()) {
            comp.setEquipement(equipement);
        }
        equipementRepository.save(equipement1);



        return new ResponseEntity<>(new ResponseMessage("equipement registered successfully!"), HttpStatus.OK);
    }

    @DeleteMapping("/equipements/{NS}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> deleteEquipement( @PathVariable String NS) {
        Equipement equipement= equipementRepository.findByEquipementNS(NS);
        equipement.setExiste(false);

        equipementRepository.save(equipement);


        return new ResponseEntity<>(new ResponseMessage("Equipement deleted successfully!"), HttpStatus.OK);

    }


//    @PostMapping("/Equipements/{codeStation}/Equipement")
//    @PreAuthorize("hasRole('ADMIN')" )
//    public ResponseEntity<?> saveEquipement(@Valid @RequestBody Equipement equipement,@PathVariable String codeStation) {
//
//        Station station = stationRepository.findByCodeStation(codeStation);
//        equipement.setStation(station);
//        equipementRepository.save(equipement);
//
//        return new ResponseEntity<>(new ResponseMessage("equipement registred successfully!"), HttpStatus.OK);
//    }



}
