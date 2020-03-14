package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.DemandePDRLigneForm;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DocumentsAPIs {


    @Autowired
    PdrRepository pdrRepository;
    @Autowired
    DemandeRepository demandeRepository;
    @Autowired
    OrdreRepository ordreRepository;
    @Autowired
    DemandePdrRepository demandePdrRepository;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    ChefStationRepository chefStationRepository;

    @Autowired
    CadreRepository cadreRepository;

    @Autowired
    PanneRepository panneRepository;

    @Autowired
    EquipementRepository equipementRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/Documents/{username}/DemandesDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEFSTATION')" )
    public List<DemandeDeTravail> getDemandeDeTravail(@PathVariable String username) {
        return demandeRepository.findAllByEmetteur_Username(username);
    }

    @GetMapping("/Documents/{username}/OrdresDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public List<OrdreDeTravail> getOrdresDeTravail(@PathVariable String username) {
        return ordreRepository.findAllByCadre_Username(username);
    }
    @GetMapping("/Documents/{codeStation}/DemandesDeTravail/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMMERCIAL')" )
    public List<DemandeDeTravail> getDemandeDeTravailofStationAndUsername(@PathVariable String username,@PathVariable String codeStation ) {
        List<DemandeDeTravail> list = demandeRepository.findAllByEmetteur_Username(username);
        List<DemandeDeTravail> listfinal = new ArrayList<>();
        for (DemandeDeTravail demandeDeTravail : list){
            if(demandeDeTravail.getPanne().getEquipement().getStation().getCodeStation().equals(codeStation)) {
                listfinal.add(demandeDeTravail);
            }
        }
        return listfinal;
    }

    @PostMapping("/Documents/{codeStation}/DemandesDeTravail/{username}")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')" )
    public ResponseEntity<?> saveDemandeDeTravail(@RequestBody Panne panne,@PathVariable String codeStation,@PathVariable String username) {
        DemandeDeTravail unedemande = new DemandeDeTravail();
        Utilisateur utilisateur = userRepository.findByUsername(username).get();
        System.out.println("description");
        System.out.println(panne);
        Station station = stationRepository.findByCodeStation(codeStation);
        incrementNbPanneOfEquipement(panne,station);
        panneRepository.save(panne);
        unedemande.setPanne(panne);
        unedemande.setEmetteur(utilisateur);
        demandeRepository.save(unedemande);
        return new ResponseEntity<>(new ResponseMessage("Demande de travail registred successfully!"), HttpStatus.OK);
    }

    private void incrementNbPanneOfEquipement(Panne panne,Station station) {
        Equipement equipement = panne.getEquipement();
        for (Composant comp:equipement.getComposants()) {
            comp.setEquipement(equipement);
        }
        int nbpanne = equipement.getNbPanne();
        equipement.setNbPanne(nbpanne+1);
        equipement.setStation(station);
        equipementRepository.save(equipement);
    }
    private void decrementNbPanneOfEquipement(Panne panne,Station station) {
        Equipement equipement = panne.getEquipement();
        for (Composant comp:equipement.getComposants()) {
            comp.setEquipement(equipement);
        }
        int nbpanne = equipement.getNbPanne();
        equipement.setNbPanne(nbpanne-1);
        equipement.setStation(station);
        equipementRepository.save(equipement);

    }

    @DeleteMapping("/Documents/DemandesDeTravail/{idDocument}")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')" )
    public ResponseEntity<?> deleteDemandeDeTravail(@PathVariable Long idDocument) {
        DemandeDeTravail document = demandeRepository.findById(idDocument).get();
        if (document.getTraite() == false) {
        decrementNbPanneOfEquipement(document.getPanne(),document.getPanne().getEquipement().getStation());
        documentRepository.deleteById(idDocument);
        panneRepository.deleteById(document.getPanne().getIdPanne());
        return new ResponseEntity<>(new ResponseMessage("Demande de travail deleted successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Demande de travail ne peut pas etre supprime"), HttpStatus.OK);
        }
    }

    @GetMapping("/Documents/DemandesDeTravail/{idDemandeDetravail}/panne")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')")
    public Panne getPanneFromDocument(@PathVariable Long idDemandeDetravail) {
        DemandeDeTravail document = demandeRepository.findById(idDemandeDetravail).get();
        //  System.out.println(document.getPanne().getDescription());
        return document.getPanne();
    }

    @PutMapping("/Panne")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')")
    public ResponseEntity<?> updatePanne(@RequestBody Panne panne) {
        Panne panne1 = panneRepository.findById(panne.getIdPanne()).get();
        decrementNbPanneOfEquipement(panne1,panne1.getEquipement().getStation());
        incrementNbPanneOfEquipement(panne,panne1.getEquipement().getStation());
        panne1.setEquipement(panne.getEquipement());
        panne1.setComposants(panne.getComposants());
        panne1.setTypePanne(panne.getTypePanne());
        panne1.setDescription(panne.getDescription());
        panneRepository.save(panne1);
        return new ResponseEntity<>(new ResponseMessage("Demande de travail updated successfully!"), HttpStatus.OK);
    }

    @GetMapping("/Documents/NewDemandesDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public List<DemandeDeTravail> getAllNewDemandeDeTravail()
    {
        return demandeRepository.findAllByTraite(false);
    }

    @GetMapping("/Documents/DemandesDeTravail/{idDemande}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public DemandeDeTravail getAllNewDemandeDeTravail(@PathVariable Long idDemande) {
        return demandeRepository.findByIdDocument(idDemande);
    }

    @GetMapping("/PDRs")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public List<PDR> getAllPDR() {
//        System.out.println("new demandes"+demandeRepository.findAllByTraite(false));
        return pdrRepository.findAll();
    }


    @GetMapping("/DemandesByStation/{codeStation}")
    @PreAuthorize("hasRole('CADRE')")
    public List<DemandeDeTravail> getDemandesByStation(@PathVariable String codeStation) {
//        System.out.println("new demandes"+demandeRepository.findAllByTraite(false));
        return demandeRepository.findByPanne_Equipement_Station_CodeStationAndTraite(codeStation,false);
    }


    @PostMapping("/Documents/{cadreUsername}/OrdreDeTravail")
    public OrdreDeTravail saveOrdreDeTravail(@Valid @RequestBody OrdreDeTravail ordreDeTravail,@PathVariable String cadreUsername) {

//        System.out.println("save ordre called");
//        System.out.println(ordreDeTravail);

        Cadre cadre= cadreRepository.findByUsername(cadreUsername);
        ordreDeTravail.setCadre(cadre);

        OrdreDeTravail ordreDeTravail1 =  ordreRepository.save(ordreDeTravail);
        for (DemandeDeTravail demandeDeTravail:ordreDeTravail.getDemandeDeTravails()
        ) {
            demandeDeTravail.setOrdreDeTravail(ordreDeTravail);
            demandeDeTravail.setTraite(true);
            demandeRepository.save(demandeDeTravail);
        }
        return ordreDeTravail1;
    }

    @PostMapping("/Documents/{cadreUsername}/DemandePDR/{idOrdre}")
    public ResponseEntity<?> saveDemandePDR(@Valid @RequestBody List<DemandePDRLigneForm>demandePDRlignesForms, @PathVariable String cadreUsername,@PathVariable Long idOrdre) {
        OrdreDeTravail ordreDeTravail = ordreRepository.findById(idOrdre).get();
        System.out.println("save demandePDR called");
        System.out.println(demandePDRlignesForms);
        Cadre cadre= cadreRepository.findByUsername(cadreUsername);
        DemandePDR demandePDR = new DemandePDR();
        demandePDR.setCadre(cadre);
        List<DemandePDRligne>demandePDRlignes = new ArrayList<>();
        for (DemandePDRLigneForm dl:demandePDRlignesForms
        ) {
            demandePDRlignes.add(new DemandePDRligne(dl.getIdLigne(),pdrRepository.findByIdPDR(dl.getIdPdr()),dl.getQuantite(),demandePDR));
        }
        demandePDR.setDemandePDRlignes(demandePDRlignes);
        System.out.println("ooooooooooooooooooooorde"+ordreDeTravail.getIdDocument());

        ordreDeTravail.setDemandePDR(demandePDR);
        ordreRepository.save(ordreDeTravail);


        return new ResponseEntity<>(new ResponseMessage("Demande PDR registred successfully!"), HttpStatus.OK);
    }




}
