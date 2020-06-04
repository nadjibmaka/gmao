package com.naftal.gmao.controller;

import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.PDR;
import com.naftal.gmao.model.Panne;
import com.naftal.gmao.repository.PdrRepository;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PDRAPIs {


    @Autowired
    PdrRepository pdrRepository;

    @GetMapping("/PDRs/{idPDR}")
    @PreAuthorize(" hasRole('MAGASINIER')" )
    public PDR getPDR(@PathVariable Long idPDR) {
        return pdrRepository.findByIdPDR(idPDR);
    }

    @GetMapping("/PDRs")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CADRE') or hasRole('MAGASINIER')" )
    public List<PDR> getAllPDRs() {
        return pdrRepository.findAll();
    }

    @PostMapping("/SavePDR")
    @PreAuthorize("hasRole('MAGASINIER')")
    public ResponseEntity<?> savePDR(@Valid @RequestBody PDR pdr) {

        if(pdrRepository.findByIdPDR(pdr.getIdPDR())!=null)
            return new ResponseEntity<>(new ResponseMessage("PDR exists"), HttpStatus.OK);

        pdrRepository.save(pdr);

        return new ResponseEntity<>(new ResponseMessage("PDR registred successfully!"), HttpStatus.OK);
    }
    @PutMapping("/UpdatePDR")
    @PreAuthorize("hasRole('MAGASINIER')")
    public ResponseEntity<?> updatePDR(@RequestBody PDR pdr) {
//        System.out.println(pdr);
        PDR pdr1 = pdrRepository.findByIdPDR(pdr.getIdPDR());
        pdr1.setDesignation(pdr.getDesignation());
        pdr1.setMarque(pdr.getMarque());
        pdr1.setPrix(pdr.getPrix());
        pdrRepository.save(pdr1);


        return new ResponseEntity<>(new ResponseMessage("PDR updated successfully!"), HttpStatus.OK);
    }


}
