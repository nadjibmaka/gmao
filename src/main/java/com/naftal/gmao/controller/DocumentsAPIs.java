package com.naftal.gmao.controller;

import com.naftal.gmao.message.request.DemandePDRLigneForm;
import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DocumentsAPIs {

    private final List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());



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

    @Autowired
    IntervenantRepository intervenantRepository;

    @Autowired
    ComposantRepositoty composantRepositoty;


    @GetMapping("/Documents/DemandesDeTravail")
    @PreAuthorize("hasRole('ADMIN')" )
    public List<DemandeDeTravail> getAllDemandeDeTravail() {
        return demandeRepository.findAll();
    }

    @GetMapping("/Documents/{username}/DemandesDeTravail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEFSTATION')" )
    public List<DemandeDeTravail> getDemandeDeTravail(@PathVariable String username) {
        return demandeRepository.findAllByEmetteur_UsernameOrderByDateDesc(username);
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
//        System.out.println("ordre update"+ ordreDeTravail);

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
    @GetMapping("/Documents/OrdresDeTravail")
    @PreAuthorize("hasRole('ADMIN')" )
    public List< OrdreDeTravail> getAllOrdreDeTravail() {
        return ordreRepository.findAll();
    }


    @GetMapping("/Documents/{codeStation}/DemandesDeTravail/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMMERCIAL')" )
    public List<DemandeDeTravail> getDemandeDeTravailofStationAndUsername(@PathVariable String username,@PathVariable String codeStation ) {
        List<DemandeDeTravail> list = demandeRepository.findAllByEmetteur_UsernameOrderByDateDesc(username);
//        System.out.println(list);
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
//        System.out.println("description");
//        System.out.println(panne);
        Station station = stationRepository.findByCodeStation(codeStation);
        incrementNbPanneOfEquipement(panne,station);
        panneRepository.save(panne);
        unedemande.setPanne(panne);
        unedemande.setEmetteur(utilisateur);

        demandeRepository.save(unedemande);

        synchronized (this.sseEmitters) {
            for (SseEmitter sseEmitter : this.sseEmitters) {
                try {
//                    System.out.println("inside the methode =====================================");
                    sseEmitter.send("created"+unedemande.getIdDocument(), MediaType.APPLICATION_JSON);
                    sseEmitter.complete();
                } catch (Exception e) {
                    //???
                }
            }
        }


        return new ResponseEntity<>(new ResponseMessage("Demande de travail registred successfully!"), HttpStatus.OK);
    }

    @GetMapping("/DemandeDeTravail/subscribe")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('CADRE')" )
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter();
        synchronized (this.sseEmitters) {
            this.sseEmitters.add(sseEmitter);
            sseEmitter.onCompletion(() -> {
                synchronized (this.sseEmitters) {
                    this.sseEmitters.remove(sseEmitter);
                }
            });
            sseEmitter.onTimeout(sseEmitter::complete);
        }
        return sseEmitter;
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
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL') or hasRole('CADRE')or hasRole('ADMIN')")
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
//        System.out.println("=====================================================");
//        System.out.println(demandePDR.getDemandePDRlignes().get(0).getPdr().getPrix());
        DemandePDR demandePDR1 = demandePdrRepository.findById(demandePDR.getIdDocument()).get();
        for (DemandePDRligne demandePDRligne: demandePDR.getDemandePDRlignes()) {
            demandePDRligne.setDemandePDR(demandePDR1);
            PDR pdr = pdrRepository.findByIdPDR(demandePDRligne.getPdr().getIdPDR());
            if (demandePDRligne.getPdr().getPrix() != pdr.getPrix()) {
                pdrRepository.save(demandePDRligne.getPdr());
            }
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
        return demandeRepository.findAllByTraiteOrderByDateDesc(false);
    }

    @GetMapping("/Documents/DemandesDeTravail/{idDemande}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE') or hasRole('CHEFSTATION')" )
    public DemandeDeTravail getDemandeDeTravail(@PathVariable Long idDemande) {
        return demandeRepository.findByIdDocument(idDemande);
    }

    @GetMapping("/DemandesPDR")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<DemandePDR> getAllDemandesPDR() {
        return demandePdrRepository.findAllByMagasinierAndAndOrdreDeTravail_TraiteOrderByDateDesc(null,true);
    }
    @GetMapping("/AllDemandesPDR")
    @PreAuthorize("hasRole('ADMIN') " )
    public List<DemandePDR> getAllDemandesPDRAdmin() {
        return demandePdrRepository.findAll();
    }
    @GetMapping("/DemandesPDR/{username}/valides")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<DemandePDR> getAllDemandesPDRValides(@PathVariable String username) {
        return demandePdrRepository.findAllByMagasinier_UsernameAndTraiteOrderByDateDesc(username,true);
    }
    @GetMapping("/DemandesPDR/{username}/Nonvalides")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MAGASINIER')" )
    public List<FicheDeTravaux> getAllDemandesPDRNonValides(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_DemandePDR_Magasinier_UsernameAndOrdreDeTravail_DemandePDR_TraiteOrderByDateDesc(username,false);
    }


    @PutMapping("/ValiderDemandePDR/{idFiche}")
    @PreAuthorize("hasRole('MAGASINIER')")
    public ResponseEntity<?> validerDemandePDR( @PathVariable Long idFiche) {
        FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idFiche).get();
        ficheDeTravaux.setValideMagasinier(true);
        ficheDeTravaux.setValidationMagasinierdate(new Date());

        DemandePDR demandePDR1 = demandePdrRepository.findById(ficheDeTravaux.getOrdreDeTravail().getDemandePDR().getIdDocument()).get();
        demandePDR1.setTraite(true);

        if (ficheDeTravaux.isValideCadre()) ficheDeTravaux.setTraite(true);

        demandePdrRepository.save(demandePDR1);
        ficheRepository.save(ficheDeTravaux);
        return new ResponseEntity<>(new ResponseMessage("Demande pdr updated successfully!"), HttpStatus.OK);

    }




    @GetMapping("/DemandesByStation/{codeStation}")
    @PreAuthorize("hasRole('CADRE')")
    public List<DemandeDeTravail> getDemandesByStation(@PathVariable String codeStation) {
//        System.out.println("new demandes"+demandeRepository.findAllByTraite(false));
        return demandeRepository.findByPanne_Equipement_Station_CodeStationAndTraiteOrderByDateDesc(codeStation,false);
    }
@GetMapping("/DerniereDemandePourStation/{codeStation}")
    @PreAuthorize("hasRole('CADRE')")
    public DemandeDeTravail getDerniereDemandeTraitePourStation(@PathVariable String codeStation) {

        return demandeRepository.findTopByPanne_Equipement_Station_CodeStationAndTraiteOrderByDateDesc(codeStation,true);
    }

    @GetMapping("/NouvelleFiche/{username}")
    @PreAuthorize("hasRole('INTERVENANT') or hasRole('ADMIN')")
    public List<FicheDeTravaux> getFicheParIntervenant(@PathVariable String username) {
        List<OrdreDeTravail> ordreDeTravails = ordreRepository.findAllByIntervenants_UsernameAndTraiteOrderByDateDesc(username,true);
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
        List<OrdreDeTravail> ordreDeTravails = ordreRepository.findAllByIntervenants_UsernameAndTraiteOrderByDateDesc(username,true);
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

//        System.out.println("save ordre called");
//       System.out.println(ordreDeTravail);

        Cadre cadre= cadreRepository.findByUsername(cadreUsername);
        ordreDeTravail.setCadre(cadre);

        for (Intervenant intervenant :ordreDeTravail.getIntervenants()){
            Intervenant intervenant1 = intervenantRepository.findByMatricule(intervenant.getMatricule());
            intervenant1.setDisponible(false);
            intervenantRepository.save(intervenant1);
        }

        for (DemandeDeTravail demandeDeTravail: ordreDeTravail.getDemandeDeTravails()){
            Equipement equipement= equipementRepository.findByEquipementNS(demandeDeTravail.getPanne().getEquipement().getEquipementNS());
            equipement.setDateDernierePanne(new Date());
//            for (Composant composant : equipement.getComposants()){
//                System.out.println(composant.getDesignation());
//
//                composant.setEquipement(equipement);
//                composantRepositoty.save(composant);
//            }
//

            equipementRepository.save(equipement);
        }






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
//        System.out.println("save demandePDR called");
//        System.out.println(demandePDRlignesForms);
        Cadre cadre= cadreRepository.findByUsername(cadreUsername);
        DemandePDR demandePDR = new DemandePDR();
        demandePDR.setCadre(cadre);
        List<DemandePDRligne>demandePDRlignes = new ArrayList<>();
        for (DemandePDRLigneForm dl:demandePDRlignesForms
        ) {
            demandePDRlignes.add(new DemandePDRligne(dl.getIdLigne(),pdrRepository.findByIdPDR(dl.getIdPdr()),dl.getQuantiteDemande(),0,0,demandePDR));


        }
        demandePDR.setDemandePDRlignes(demandePDRlignes);
//        System.out.println("ooooooooooooooooooooorde"+ordreDeTravail.getIdDocument());

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

         final String ACCOUNT_SID = "AC6c4303c22d93d9db6a6a0b6c530ce547";
         final String AUTH_TOKEN = "a5dd8566573bcf43c7f5b6dd85977195";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//        for(Intervenant inter : ficheDeTravaux.getOrdreDeTravail().getIntervenants()){
//            String numero = "+213"+inter.getNumTelephone().substring(1);
//            System.out.println(numero);
//            Message message = Message.creator(new PhoneNumber(numero),
//                    new PhoneNumber("+15109372091"),
//                    "une nouvelle fiche de traveaux a été crée").create();
//            System.out.println(message.getSid());
//        }
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
         ficheDeTravaux1.setRemplissageIntervenantdate(new Date());
        ficheRepository.save(ficheDeTravaux1);

        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);
    }


    @GetMapping("/NouvelleFiche/ChefStation/{username}")
    @PreAuthorize("hasRole('CHEFSTATION')")
    public List<FicheDeTravaux> getFicheNouvelleDeChef(@PathVariable String username) {
        return ficheRepository.findAllByStation_ChefStation_UsernameAndValideChefAndRempliOrderByDateDesc(username,false,true);
    }


    @GetMapping("/NouvelleFiche/Commercial/{username}")
    @PreAuthorize("hasRole('COMMERCIAL')")
    public List<FicheDeTravaux> getFicheNouvelleDeCommercial(@PathVariable String username) {
        List<FicheDeTravaux> listefinale = new ArrayList<>();
    Utilisateur utilisateur = userRepository.findByUsername(username).get();
    List<FicheDeTravaux> list = ficheRepository.findAllByValideChefAndRempliOrderByDateDesc(false,true);
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
        List<FicheDeTravaux> list = ficheRepository.findAllByValideChefAndRempliOrderByDateDesc(true,true);
        for (FicheDeTravaux ficheDeTravaux: list){
            if (ficheDeTravaux.getOrdreDeTravail().getDemandeDeTravails().get(0).getEmetteur() == utilisateur)
                listefinale.add(ficheDeTravaux);
        }
        return listefinale;
    }

    @GetMapping("/Documents/AllFiches")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FicheDeTravaux> getAllfichesPourAdminStatistiques() {
        return ficheRepository.findAll();
    }



    @GetMapping("/AllFiche")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FicheDeTravaux> getAllFichesTravaux() {
        return ficheRepository.findAll();
    }

    @GetMapping("/NouvelleFiche/Cadre/{username}")
    @PreAuthorize("hasRole('CADRE')")
    public List<FicheDeTravaux> getFicheNouvelleDeCadre(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_Cadre_UsernameAndValideCadreOrderByDateDesc(username,false);
    }

    @GetMapping("/AllFiche/ChefStation/{username}")
    @PreAuthorize("hasRole('CHEFSTATION')")
    public List<FicheDeTravaux> getAllFicheDeChef(@PathVariable String username) {
        return ficheRepository.findAllByStation_ChefStation_UsernameAndValideChefAndRempliOrderByDateDesc(username,true,true);
    }

    @GetMapping("/AllFiche/Cadre/{username}")
    @PreAuthorize("hasRole('CADRE')")
    public List<FicheDeTravaux> getAllFicheDeCadre(@PathVariable String username) {
        return ficheRepository.findAllByOrdreDeTravail_Cadre_UsernameAndValideCadreOrderByDateDesc(username,true);
    }


    @GetMapping("FicheDeTravaux/ValideParChef/{idDocument}")
    @PreAuthorize("hasRole('CHEFSTATION') or hasRole('COMMERCIAL')")
    public ResponseEntity<?> ValiderFicheParChef(@PathVariable Long idDocument) {
 FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idDocument).get();
 ficheDeTravaux.setValideChef(true);
 ficheDeTravaux.setValidationChefdate(new Date());
 for (Intervenant intervenant : ficheDeTravaux.getOrdreDeTravail().getIntervenants()){
     Intervenant intervenant1 = intervenantRepository.findByMatricule(intervenant.getMatricule());
     intervenant1.setDisponible(true);
     intervenantRepository.save(intervenant1);
 }
 ficheRepository.save(ficheDeTravaux);
        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);

    }
    @GetMapping("FicheDeTravaux/ValideParCadre/{idDocument}/{tempTotal}")
    @PreAuthorize("hasRole('CADRE')")
    public ResponseEntity<?> ValiderFicheParCadre(@PathVariable Long idDocument,@PathVariable int tempTotal) {
 FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idDocument).get();
 ficheDeTravaux.setTempTotal(tempTotal);
 ficheDeTravaux.setValideCadre(true);
 ficheDeTravaux.setValidationCadredate(new Date());
 if (ficheDeTravaux.isValideMagasinier()) ficheDeTravaux.setTraite(true);
 ficheRepository.save(ficheDeTravaux);
 inceremetIntervenantNbheure(ficheDeTravaux.getOrdreDeTravail().getIntervenants(),tempTotal);
        return new ResponseEntity<>(new ResponseMessage("fiche updated successfully!"), HttpStatus.OK);
    }

    public void inceremetIntervenantNbheure(List<Intervenant> list,int tempTotal){
        boolean found = false;
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        for (Intervenant inter: list) {
            if (inter.getNbHeurs() != null){
                      for (NbHeureLigne ligne : inter.getNbHeurs()){
                          if (ligne.getDate().equals(year+"-"+month)) {
                              found = true;
                              ligne.setNbHeure(ligne.getNbHeure() + tempTotal);
                              break;
                          }
                      }
                      if (!found) {
                          inter.getNbHeurs().add(new NbHeureLigne(year+"-"+month,tempTotal,inter));
                      }
            } else {
                List<NbHeureLigne> newList = new ArrayList<>();
                newList.add(new NbHeureLigne(year+"-"+month,tempTotal,inter));
                inter.setNbHeurs(newList);
            }
intervenantRepository.save(inter);
        }
    }



    @GetMapping("/Pannes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Panne> getAllPannes() {

        return panneRepository.findAll();

    }
}
