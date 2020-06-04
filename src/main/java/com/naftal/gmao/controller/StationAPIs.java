package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.EditForm;
import com.naftal.gmao.message.request.EditStationForm;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.ChefStationRepository;
import com.naftal.gmao.repository.EquipementRepository;
import com.naftal.gmao.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

public class StationAPIs {

    @Autowired
    StationRepository stationRepository;

    @Autowired
    ChefStationRepository chefStationRepository;

    @Autowired
    EquipementRepository equipementRepository;


    @GetMapping("/api/test/stations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Station> getStations() {

        List<Station> list = stationRepository.findAllByOrderByCodeStationAsc();
//        System.out.println(list);
        return list;
    }
    @GetMapping("/stationsWithoutChef")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Station> getStationsWithoutChef() {

        List<Station> list = stationRepository.findAllByChefStationAndExiste(null,true);
//        System.out.println(list);
        return list;
    }

    @GetMapping("/stations/{codeStation}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMMERCIAL') or hasRole('CHEFSTATION')")
    public Station getStationbyCodeStation(@PathVariable String codeStation)

    {
//        System.out.println("get station called");
//        System.out.println(codeStation);
//        System.out.println(stationRepository.findByCodeStation(codeStation));
        return stationRepository.findByCodeStation(codeStation);
    }


//    @RequestMapping(value = "/api/test/stations", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/api/test/stations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveStation(@Valid @RequestBody Station station) {
//        System.out.println("function called");
         stationRepository.save(station);
        return new ResponseEntity<>(new ResponseMessage("Station registred successfully!"), HttpStatus.OK);
    }

    @PutMapping("/stations/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerStation(@Valid @RequestBody EditStationForm editForm , @PathVariable String codeStation) {


//        System.out.println("put station called");
//        System.out.println(editForm.getRaisonSocial());

        Station station = stationRepository.findByCodeStation(codeStation);
        station.setTempTrajet(editForm.getTempTrajet());
        station.setRaisonSocial(editForm.getRaisonSocial());
        station.setDistrict(editForm.getDistrict());
        station.setDistance(editForm.getDistance());
        station.setAdresse(editForm.getAdresse());
        station.setLatitude(editForm.getLatitude());
        station.setLongitude(editForm.getLongitude());

        TypeStation typeStation;
        switch (editForm.getTypeStation()){
            case "GL":
                typeStation=TypeStation.GL;
                station.setTypeStation(typeStation);
                break;

            case "GD":
                typeStation=TypeStation.GD;
                station.setTypeStation(typeStation);
                break;

            case "PVA":
                typeStation=TypeStation.PVA;
                station.setTypeStation(typeStation);
                break;
        }

        Secteur secteur;
        switch (editForm.getSecteur()){
            case "Ghelizane":
                secteur=Secteur.Ghelizane;
                station.setSecteur(secteur);
                break;
            case "Tiaret":
                secteur=Secteur.Tiaret;
                station.setSecteur(secteur);
                break;
            case "Chlef":
                secteur=Secteur.Chlef;
                station.setSecteur(secteur);
                break;
        }

        stationRepository.save(station);


        return new ResponseEntity<>(new ResponseMessage("Utilisateur registered successfully!"), HttpStatus.OK);
    }

    @DeleteMapping("/stations/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> deleteStation( @PathVariable String codeStation) {
        Station station = stationRepository.findByCodeStation(codeStation);
        station.setExiste(false);
        ChefStation chefStation= station.getChefStation();
        if (chefStation!=null){
           chefStation= chefStationRepository.findByMatricule(chefStation.getMatricule());
            chefStation.setStation(null);
            chefStation.setActivated(false);
            chefStationRepository.save(chefStation);


        }
        stationRepository.save(station);

        return new ResponseEntity<>(new ResponseMessage("station deleted successfully!"), HttpStatus.OK);

    }

    @DeleteMapping("/stations/chefStation/{matricule}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> removeChefStation( @PathVariable String matricule) {
        ChefStation chefStation= chefStationRepository.findByMatricule(matricule);

            chefStation.setStation(null);
            chefStation.setActivated(false);
            chefStationRepository.save(chefStation);


        return new ResponseEntity<>(new ResponseMessage("chef station removed successfully!"), HttpStatus.OK);

    }

    @GetMapping("/stations/activate/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?>activateStation( @PathVariable String codeStation) {
        Station station= stationRepository.findByCodeStation(codeStation);
        station.setExiste(true);
        stationRepository.save(station);

        return new ResponseEntity<>(new ResponseMessage("station activated successfully!"), HttpStatus.OK);

    }

    @GetMapping("/stations/deactivate/{codeStation}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<?> deactivateStation( @PathVariable String codeStation) {
        Station station= stationRepository.findByCodeStation(codeStation);
        station.setExiste(false);
        stationRepository.save(station);

        return new ResponseEntity<>(new ResponseMessage("station deactivated successfully!"), HttpStatus.OK);
    }

    @GetMapping("/stations/equipements/{codeStation}")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL') or hasRole('ADMIN') or hasRole('CADRE')")
    public List<Equipement> getStationEquipements(@PathVariable String codeStation)
    {
        return equipementRepository.findByStationCodeStationOrderByEquipementNS(codeStation);
    }

    @GetMapping("/stations/equipements/{equipement}/composants")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL') or hasRole('CADRE')")
    public List<Composant> getComposantsOfEquipements(@PathVariable String equipement)

    {
        Equipement equipement1 = equipementRepository.findById(equipement).get();
       List<Composant> list = new ArrayList<>();
       list.addAll(equipement1.getComposants());
        return  list;
           }


    @GetMapping("/GLandPVAstations")
    @PreAuthorize("hasRole('COMMERCIAL')")
    public List<Station> getGlandPVAStations() {

        List<Station> listGL = stationRepository.findAllByTypeStation(TypeStation.GL);
        List<Station> listPVA = stationRepository.findAllByTypeStation(TypeStation.PVA);
        List<Station> all = new ArrayList<>();
        all.addAll(listGL);
        all.addAll(listPVA);
        return all;
    }

    @GetMapping("/station/{username}")
    @PreAuthorize("hasRole('CHEFSTATION')")
    public Station getStationFromUsername(@PathVariable String username) {

        return stationRepository.findByChefStation_Username(username);
    }

    @GetMapping("/station/demandes")
    public List<Station> getStationWithDemande() {
        return stationRepository.findByDemande();
    }


}
