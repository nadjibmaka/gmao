package com.naftal.gmao.controller;


import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.*;
import com.naftal.gmao.repository.*;
import com.naftal.gmao.services.jasperReports;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.http.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ImpressionAPIs {

    @Autowired
    jasperReports jasperReports;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FicheRepository ficheRepository;

    @Autowired
    OrdreRepository ordreRepository;

    @Autowired
    DemandeRepository demandeRepository;

    @Autowired
    DemandePdrRepository demandePdrRepository;


    @GetMapping(value = "/Utilisateurs/imprimer", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public String printUsers() throws IOException, JRException {
List<Utilisateur> utilisateurs = userRepository.findAll();
return jasperReports.exportEmployeReport(utilisateurs);
    }

    @GetMapping(value = "/FicheTravaux/{idFiche}/imprimer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')")
    public ResponseEntity<?> printFicheTravaux(@PathVariable Long idFiche) throws IOException, JRException {
        FicheDeTravaux ficheDeTravaux = ficheRepository.findById(idFiche).get();
List<FicheDeTravaux> list = new ArrayList<>();
list.add(ficheDeTravaux);
        byte [] content = jasperReports.exportFicheTravauxReport(list);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-pdf"))
                .body(content);
    }

    @GetMapping(value = "/OrdreDeTravail/{idFiche}/imprimer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE')")
    @ResponseBody
    public ResponseEntity<?> printOrdreDeTravail(@PathVariable Long idFiche) throws IOException, JRException {
        OrdreDeTravail ordreDeTravail = ordreRepository.findById(idFiche).get();
        List<OrdreDeTravail> ordres = new ArrayList<>();
        ordres.add(ordreDeTravail);
        byte [] content = jasperReports.exportOrdreReport(ordres);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-pdf"))
                .body(content);


    }

    @GetMapping(value = "/DemandeDeTravail/{idFiche}/imprimer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE') or hasRole('CHEFSTATION')")
    public ResponseEntity<?> printDemandeDeTravail(@PathVariable Long idFiche) throws IOException, JRException {

        DemandeDeTravail demandeDeTravail = demandeRepository.findByIdDocument(idFiche);
        List<DemandeDeTravail> list = new ArrayList<>();
        list.add(demandeDeTravail);
        byte [] content = jasperReports.exportDemandeReport(list);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-pdf"))
                .body(content);
    }

    @GetMapping(value = "/DemandePDR/{idFiche}/imprimer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE') or hasRole('MAGASINIER')")
    public ResponseEntity<?> printDemandePDR(@PathVariable Long idFiche) throws IOException, JRException {

        DemandePDR demandePDR = demandePdrRepository.findById(idFiche).get();
        List<DemandePDR> list = new ArrayList<>();
        list.add(demandePDR);
        byte [] content = jasperReports.exportDemandePDRReport(list);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-pdf"))
                .body(content);
    }




}
