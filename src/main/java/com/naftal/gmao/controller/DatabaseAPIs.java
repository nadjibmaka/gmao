package com.naftal.gmao.controller;

import com.naftal.gmao.message.response.ResponseMessage;
import com.naftal.gmao.model.Station;
import com.naftal.gmao.repository.EquipementRepository;
import com.naftal.gmao.repository.PdrRepository;
import com.naftal.gmao.repository.StationRepository;
import com.naftal.gmao.services.CSVService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.jfree.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DatabaseAPIs {


    @Autowired
    CSVService csvService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    EquipementRepository equipementRepository;

    @Autowired
    PdrRepository pdrRepository;


    @GetMapping("/saveStations")
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<Resource> SaveStations() throws IOException {
        InputStreamResource file = new InputStreamResource(csvService.exportStationFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stationsDB.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }


    @PostMapping("/uploadStations")
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<?> uploadStations(@Valid @RequestBody MultipartFile file) throws IOException {
         if (stationRepository.count()==0) {
             csvService.uploadStationFile(file);}
         else {
             return new ResponseEntity<>(new ResponseMessage("table des station existe deja"), HttpStatus.NOT_ACCEPTABLE);

         }
        return new ResponseEntity<>(new ResponseMessage("stations saved successfully!"), HttpStatus.OK);
    }

    @GetMapping("/saveEquipements")
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<Resource> SaveEquipements() throws IOException {
        InputStreamResource file = new InputStreamResource(csvService.exportEquipementsFile());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipementsDB.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }


    @PostMapping("/uploadEquipements")
    @PreAuthorize("hasRole('ADMIN')" )
    public ResponseEntity<?> uploadEquipements(@Valid @RequestBody MultipartFile file) throws IOException, ParseException {

        if (stationRepository.count()>0) {
            if (equipementRepository.count()==0) {
            csvService.uploadEquipementsFile(file);}
        else {
            return new ResponseEntity<>(new ResponseMessage("table des equipements existe deja"), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(new ResponseMessage("Equipements saved successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Tables des stations vide"), HttpStatus.NOT_ACCEPTABLE);
        }
    }



    @GetMapping("/savePDR")
    @PreAuthorize("hasRole('MAGASINIER') or hasRole('ADMIN')" )
    public ResponseEntity<Resource> SavePDR() throws IOException {
        InputStreamResource file = new InputStreamResource(csvService.exportPDRFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pdrDB.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }


    @PostMapping("/uploadPDR")
    @PreAuthorize("hasRole('MAGASINIER') or hasRole('ADMIN')" )
    public ResponseEntity<?> uploadPDR(@Valid @RequestBody MultipartFile file) throws IOException {
        if (pdrRepository.count()==0) {
            csvService.uploadPDRFile(file);}
        else {
            return new ResponseEntity<>(new ResponseMessage("table des pdr existe deja"), HttpStatus.NOT_ACCEPTABLE);

        }
        return new ResponseEntity<>(new ResponseMessage("pdr saved successfully!"), HttpStatus.OK);
    }








}
