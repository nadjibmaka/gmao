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
    FicheRepository ficheRepository;

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
    @Autowired
    MagasinierRepository magasinierRepository;
    @Autowired
    DemandePDRLigneRepository demandePDRLigneRepository;

    @GetMapping("/Documents/{username}/DemandesDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEFSTATION')" )
    public List<DemandeDeTravail> getDemandeDeTravail(@PathVariable String username) {
        return demandeRepository.findAllByEmetteur_Username(username);
    }

    @GetMapping("/Documents/{username}/OrdresDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public List<OrdreDeTravail> getOrdresDeTravail(@PathVariable String username) {
        return ordreRepository.findAllByCadre_UsernameOrderByDateDesc(username);
    }
    @PutMapping("/Documents/OrdreDeTravail/{idOrdre}/traite")
    @PreAuthorize("hasRole('CADRE') or hasRole('ADMIN')")
    public ResponseEntity<?> OrdreDeTravailTraite( @PathVariable Long idOrdre) {

        OrdreDeTravail ordreDeTravail1= ordreRepository.findById(idOrdre).get();
        ordreDeTravail1.setTraite(true);

        ordreRepository.save(ordreDeTravail1);
        return  new ResponseEntity<>(new ResponseMessage("Ordre de Travail updated traite successfully!"), HttpStatus.OK);
    }


    @PutMapping("/Documents/{cadreUsername}/OrdreDeTravail")
    @PreAuthorize("hasRole('CADRE') or hasRole('ADMIN')")
    public ResponseEntity<?> updateOrdreDeTravail(@RequestBody OrdreDeTravail ordreDeTravail, @PathVariable String cadreUsername) {
        System.out.println("ordre update"+ ordreDeTravail);

        OrdreDeTravail ordreDeTravail1= ordreRepository.findById(ordreDeTravail.getIdDocument()).get();
        Cadre cadre = cadreRepository.findByUsername(cadreUsername);
        ordreDeTravail1=ordreDeTravail;
        ordreDeTravail1.setCadre(cadre);

        ordreRepository.save(ordreDeTravail1);
        return  new ResponseEntity<>(new ResponseMessage("Ordre de Travail updated successfully!"), HttpStatus.OK);
    }

    @GetMapping("/Documents/OrdresDeTravail/{idOrdre}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')" )
    public OrdreDeTravail getOrdreDeTravail(@PathVariable Long idOrdre) {
        return ordreRepository.findById(idOrdre).get();
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

    @PutMapping("/UpdateLigneDemandePDR/{magasinier}")
    @PreAuthorize("hasRole('MAGASINIER')")
    public ResponseEntity<?> updateLigne(@RequestBody DemandePDR demandePDR, @PathVariable String magasinier) {
        DemandePDR demandePDR1 = demandePdrRepository.findById(demandePDR.getIdDocument()).get();
        for (DemandePDRligne demandePDRligne: demandePDR.getDemandePDRlignes()) {
            demandePDRligne.setDemandePDR(demandePDR1);
        }
        demandePDR1.setDemandePDRlignes(demandePDR.getDemandePDRlignes());
        Magasinier magasinier1 = magasinierRepository.findByUsername(magasinier);
        demandePDR1.setMagasinier(magasinier1);
        demandePdrRepository.save(demandePDR1);
        return new ResponseEntity<>(new ResponseMessage("Demande pdr updated successfully!"), HttpStatus.OK);

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

    @GetMapping("/DemandesPDR")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<DemandePDR> getAllDemandesPDR() {
        return demandePdrRepository.findAllByMagasinierAndAndOrdreDeTravail_Traite(null,true);
    }
    @GetMapping("/DemandesPDR/{username}/valides")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<DemandePDR> getAllDemandesPDRValides(@PathVariable String username) {
        return demandePdrRepository.findAllByMagasinier_UsernameAndTraite(username,true);
    }
    @GetMapping("/DemandesPDR/{username}/Nonvalides")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<FicheDeTravaux> getAllDemandesPDRNonValides(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_DemandePDR_Magasinier_UsernameAndOrdreDeTravail_DemandePDR_Traite(username,false);
    }


    @PutMapping("/ValiderDemandePDR/{idFiche}")
    @PreAuthorize("hasRole('MAGASINIER')")
    public ResponseEntity<?> validerDemandePDR( @PathVariable Long idFiche) {
        FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idFiche).get();
        ficheDeTravaux.setValideMagasinier(true);

        DemandePDR demandePDR1 = demandePdrRepository.findById(ficheDeTravaux.getOrdreDeTravail().getDemandePDR().getIdDocument()).get();
        demandePDR1.setTraite(true);

        if (ficheDeTravaux.isValideCadre()) ficheDeTravaux.setTraite(true);

        demandePdrRepository.save(demandePDR1);
        ficheRepository.save(ficheDeTravaux);
        return new ResponseEntity<>(new ResponseMessage("Demande pdr updated successfully!"), HttpStatus.OK);

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

    @GetMapping("/NouvelleFiche/{username}")
    @PreAuthorize("hasRole('INTERVENANT') or hasRole('ADMIN')")
    public List<FicheDeTravaux> getFicheParIntervenant(@PathVariable String username) {
        List<OrdreDeTravail> ordreDeTravails = ordreRepository.findAllByIntervenants_UsernameAndTraite(username,true);
        List<FicheDeTravaux> ficheDeTravauxes = new ArrayList<>();
        for (OrdreDeTravail ordreDeTravail : ordreDeTravails) {
            FicheDeTravaux ficheDeTravaux = ficheRepository.findByOrdreDeTravailAndRempli(ordreDeTravail,false);
            if(ficheDeTravaux != null) ficheDeTravauxes.add(ficheDeTravaux);
        }
        return ficheDeTravauxes;
    }
    @GetMapping("/Documents/{username}/FichesDeTravaux")
    @PreAuthorize("hasRole('INTERVENANT') or hasRole('ADMIN')")
    public List<FicheDeTravaux> getFichesOfIntervenant(@PathVariable String username) {
        List<OrdreDeTravail> ordreDeTravails = ordreRepository.findAllByIntervenants_UsernameAndTraite(username,true);
        List<FicheDeTravaux> ficheDeTravauxes = new ArrayList<>();
        for (OrdreDeTravail ordreDeTravail : ordreDeTravails) {
            FicheDeTravaux ficheDeTravaux = ficheRepository.findByOrdreDeTravailAndRempli(ordreDeTravail,true);
            if(ficheDeTravaux != null) ficheDeTravauxes.add(ficheDeTravaux);
        }

        return ficheDeTravauxes;
    }


    @GetMapping("/Documents/FichesDeTravaux/{idFiche}")
    public FicheDeTravaux getFicheDeTravaux(@PathVariable Long idFiche) {

        FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idFiche).get();
        return ficheDeTravaux;
    }


    @GetMapping("/Documents/DemandesPDR")
    @PreAuthorize("hasRole('INTERVENANT') or hasRole('ADMIN')")
    public List<DemandePDR> getDemandePDR() {

        List<DemandePDR>demandePDRS= demandePdrRepository.findAll();
        return demandePDRS;
    }

    @PostMapping("/Documents/{cadreUsername}/OrdreDeTravail")
    @PreAuthorize("hasRole('CADRE')")
    public OrdreDeTravail saveOrdreDeTravail(@Valid @RequestBody OrdreDeTravail ordreDeTravail,@PathVariable String cadreUsername) {

        System.out.println("save ordre called");
       System.out.println(ordreDeTravail);

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
    @PreAuthorize("hasRole('CADRE')")
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
            demandePDRlignes.add(new DemandePDRligne(dl.getIdLigne(),pdrRepository.findByIdPDR(dl.getIdPdr()),dl.getQuantiteDemande(),0,0,demandePDR));


        }
        demandePDR.setDemandePDRlignes(demandePDRlignes);
        System.out.println("ooooooooooooooooooooorde"+ordreDeTravail.getIdDocument());

        demandePdrRepository.save(demandePDR);

        ordreDeTravail.setDemandePDR(demandePDR);
        ordreRepository.save(ordreDeTravail);
//        demandePDR.setOrdreDeTravail(ordreDeTravail);



        return new ResponseEntity<>(new ResponseMessage("Demande PDR registred successfully!"), HttpStatus.OK);
    }


    @PutMapping("/Documents/{cadreUsername}/DemandePDR/{idOrdre}")
    @PreAuthorize("hasRole('CADRE') or hasRole('MAGASINIER')")
    public ResponseEntity<?> updateDemandePDR(@Valid @RequestBody List<DemandePDRLigneForm>demandePDRlignesForms, @PathVariable String cadreUsername,@PathVariable Long idOrdre) {
        OrdreDeTravail ordreDeTravail = ordreRepository.findById(idOrdre).get();
        Cadre cadre= cadreRepository.findByUsername(cadreUsername);
        DemandePDR demandePDR = demandePdrRepository.findById(ordreDeTravail.getDemandePDR().getIdDocument()).get();
        demandePDR.setCadre(cadre);
        demandePDRLigneRepository.deleteAllByDemandePDR(demandePDR);

        List<DemandePDRligne>demandePDRlignes = new ArrayList<>();
        for (DemandePDRLigneForm dl:demandePDRlignesForms
        ) {
            demandePDRlignes.add(new DemandePDRligne(dl.getIdLigne(),pdrRepository.findByIdPDR(dl.getIdPdr()),dl.getQuantiteDemande(),dl.getQuantiteAccorde(),dl.getQuantiteUtilise(),demandePDR));
        }
        demandePDR.setDemandePDRlignes(demandePDRlignes);
        demandePdrRepository.save(demandePDR);
        ordreDeTravail.setDemandePDR(demandePDR);
        ordreRepository.save(ordreDeTravail);


        return new ResponseEntity<>(new ResponseMessage("Demande PDR registred successfully!"), HttpStatus.OK);
    }




    @DeleteMapping("/Documents/DemandePDR/{idOrdre}")
    @PreAuthorize("hasRole('CADRE')")
    public ResponseEntity<?> deleteDemandePDR(@Valid @PathVariable Long idOrdre) {
        OrdreDeTravail ordreDeTravail = ordreRepository.findById(idOrdre).get();
        DemandePDR demandePDR = demandePdrRepository.findById(ordreDeTravail.getDemandePDR().getIdDocument()).get();
        ordreDeTravail.setDemandePDR(null);
        ordreRepository.save(ordreDeTravail);
        demandePdrRepository.delete(demandePDR);


        return new ResponseEntity<>(new ResponseMessage("Demande PDR registred successfully!"), HttpStatus.OK);
    }



    @PostMapping("/Documents/{idOrdre}/FicheDeTravaux")
    @PreAuthorize("hasRole('CADRE')")
    public FicheDeTravaux saveFicheDeTravaux(@Valid @RequestBody FicheDeTravaux ficheDeTravaux , @PathVariable Long idOrdre) {

        OrdreDeTravail ordreDeTravail=ordreRepository.findById(idOrdre).get();
        ficheDeTravaux.setOrdreDeTravail(ordreDeTravail);
        ficheDeTravaux.setTraite(false);
        if (ordreDeTravail.getDemandePDR() == null) ficheDeTravaux.setValideMagasinier(true);
        ficheDeTravaux.setStation(ordreDeTravail.getDemandeDeTravails().get(0).getPanne().getEquipement().getStation());
        FicheDeTravaux ficheDeTravaux1 = ficheRepository.save(ficheDeTravaux);

        return ficheDeTravaux;
    }

    @PutMapping("/Documents/FicheDeTravaux/update")
    public ResponseEntity<?> updateFicheDeTravaux(@Valid @RequestBody FicheDeTravaux ficheDeTravaux) {

        FicheDeTravaux ficheDeTravaux1 = ficheRepository.findById(ficheDeTravaux.getIdDocument()).get();
        ficheDeTravaux1=ficheDeTravaux;

if (ficheDeTravaux.getOrdreDeTravail().getDemandePDR() != null) {
        DemandePDR demandePDR = demandePdrRepository.findById(ficheDeTravaux1.getOrdreDeTravail().getDemandePDR().getIdDocument()).get();

        for (DemandePDRligne demandePDRligne:ficheDeTravaux1.getOrdreDeTravail().getDemandePDR().getDemandePDRlignes()
             ) {
            demandePDRligne.setDemandePDR(demandePDR);

        }

        demandePdrRepository.save(ficheDeTravaux1.getOrdreDeTravail().getDemandePDR());
}
        ficheRepository.save(ficheDeTravaux1);

        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);
    }


    @GetMapping("/NouvelleFiche/ChefStation/{username}")
    @PreAuthorize("hasRole('CHEFSTATION')")
    public List<FicheDeTravaux> getFicheNouvelleDeChef(@PathVariable String username) {
        return ficheRepository.findAllByStation_ChefStation_UsernameAndValideChefAndRempli(username,false,true);
    }


    @GetMapping("/NouvelleFiche/Commercial/{username}")
    @PreAuthorize("hasRole('COMMERCIAL')")
    public List<FicheDeTravaux> getFicheNouvelleDeCommercial(@PathVariable String username) {
        List<FicheDeTravaux> listefinale = new ArrayList<>();
    Utilisateur utilisateur = userRepository.findByUsername(username).get();
    List<FicheDeTravaux> list = ficheRepository.findAllByValideChefAndRempli(false,true);
    for (FicheDeTravaux ficheDeTravaux: list){
        if (ficheDeTravaux.getOrdreDeTravail().getDemandeDeTravails().get(0).getEmetteur() == utilisateur)
            listefinale.add(ficheDeTravaux);
    }
        return listefinale;
    }

    @GetMapping("/AllFiche/Commercial/{username}")
    @PreAuthorize("hasRole('COMMERCIAL')")
    public List<FicheDeTravaux> getAllFicheDeCommercial(@PathVariable String username) {
        List<FicheDeTravaux> listefinale = new ArrayList<>();
        Utilisateur utilisateur = userRepository.findByUsername(username).get();
        List<FicheDeTravaux> list = ficheRepository.findAllByValideChefAndRempli(true,true);
        for (FicheDeTravaux ficheDeTravaux: list){
            if (ficheDeTravaux.getOrdreDeTravail().getDemandeDeTravails().get(0).getEmetteur() == utilisateur)
                listefinale.add(ficheDeTravaux);
        }
        return listefinale;
    }


    @GetMapping("/NouvelleFiche/Cadre/{username}")
    @PreAuthorize("hasRole('CADRE')")
    public List<FicheDeTravaux> getFicheNouvelleDeCadre(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_Cadre_UsernameAndValideCadre(username,false);
    }

    @GetMapping("/AllFiche/ChefStation/{username}")
    @PreAuthorize("hasRole('CHEFSTATION')")
    public List<FicheDeTravaux> getAllFicheDeChef(@PathVariable String username) {
        return ficheRepository.findAllByStation_ChefStation_UsernameAndValideChefAndRempli(username,true,true);
    }

    @GetMapping("/AllFiche/Cadre/{username}")
    @PreAuthorize("hasRole('CADRE')")
    public List<FicheDeTravaux> getAllFicheDeCadre(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_Cadre_UsernameAndValideCadre(username,true);
    }


    @GetMapping("FicheDeTravaux/ValideParChef/{idDocument}")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')")
    public ResponseEntity<?> ValiderFicheParChef(@PathVariable Long idDocument) {
 FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idDocument).get();
 ficheDeTravaux.setValideChef(true);
 ficheRepository.save(ficheDeTravaux);
        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);

    }
    @GetMapping("FicheDeTravaux/ValideParCadre/{idDocument}/{tempTotal}")
    @PreAuthorize("hasRole('CADRE')")
    public ResponseEntity<?> ValiderFicheParCadre(@PathVariable Long idDocument,@PathVariable int tempTotal) {
 FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idDocument).get();
 ficheDeTravaux.setTempTotal(tempTotal);
 ficheDeTravaux.setValideCadre(true);
 if (ficheDeTravaux.isValideMagasinier()) ficheDeTravaux.setTraite(true);
 ficheRepository.save(ficheDeTravaux);
        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);

    }


}
