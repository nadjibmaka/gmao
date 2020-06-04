package com.naftal.gmao.services;

import com.naftal.gmao.model.Equipement;
import com.naftal.gmao.model.PDR;
import com.naftal.gmao.model.Station;
import com.naftal.gmao.repository.EquipementRepository;
import com.naftal.gmao.repository.PdrRepository;
import com.naftal.gmao.repository.StationRepository;
import org.apache.commons.csv.*;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CSVService {
    @Autowired
    StationRepository stationRepository;

    @Autowired
    EquipementRepository equipementRepository;

    @Autowired
    PdrRepository pdrRepository;


//    public Station findstationById(String code){
//          return  stationRepository.findByCodeStation(code);
//    }

    public InputStreamReader newReader(final InputStream inputStream) {
        return new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
    }

    public void uploadStationFile(MultipartFile file) {
        try {
                BufferedReader fileReader = new BufferedReader(newReader(file.getInputStream()));
                CSVParser csvParser = new CSVParser(fileReader,CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                List<Station> stations = new ArrayList<>();
                Iterable<CSVRecord> csvRecords = csvParser.getRecords();
                for (CSVRecord csvRecord : csvRecords) {
                    Station station = new Station(
                            csvRecord.get("code_station"),
                            csvRecord.get("raison_social"),
                            Long.parseLong(csvRecord.get("distance")),
                            Long.parseLong(csvRecord.get("temp_trajet")),
                            csvRecord.get("district"),
                            csvRecord.get("secteur"),
                            csvRecord.get("adresse"),
                            csvRecord.get("type_station"),
                            Boolean.parseBoolean(csvRecord.get("existe")),
                            Double.parseDouble(csvRecord.get("longitude")),
                            Double.parseDouble(csvRecord.get("latitude"))
                    );
                    stations.add(station);
                }
                stationRepository.saveAll(stations);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportStationFile() {
        List<Station> stations = stationRepository.findAll();
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withDelimiter(';').withHeader("code_station","adresse","distance","district","existe","raison_social","secteur","temp_trajet","type_station","longitude","latitude");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Station station : stations) {
                List<String> data = Arrays.asList(
                        station.getCodeStation(),
                        station.getAdresse(),
                        String.valueOf(station.getDistance()),
                        station.getDistrict(),
                        String.valueOf(station.isExiste()),
                        station.getRaisonSocial(),
                        String.valueOf(station.getSecteur()),
                        String.valueOf(station.getTempTrajet()),
                        String.valueOf(station.getTypeStation()),
                        String.valueOf(station.getLongitude()),
                        String.valueOf(station.getLatitude())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }


    }

    public void uploadEquipementsFile(MultipartFile file) throws ParseException {
        try {
            BufferedReader fileReader = new BufferedReader(newReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<Equipement> equipements = new ArrayList<>();
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                Equipement equipement = new Equipement(
                        csvRecord.get("equipementns"),
                        csvRecord.get("designation"),
                        Integer.parseInt(csvRecord.get("nb_panne")),
                        csvRecord.get("type"),
                        csvRecord.get("marque"),
                        ft.parse(csvRecord.get("date_fabrication")),
                        Long.parseLong(csvRecord.get("prix")),
                        Long.parseLong(csvRecord.get("depenses")),
                        stationRepository.findByCodeStation(csvRecord.get("code_station")),
                        Boolean.parseBoolean(csvRecord.get("existe"))
                );
                equipements.add(equipement);
            }
                equipementRepository.saveAll(equipements);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportEquipementsFile() {
        List<Equipement> equipements = equipementRepository.findAll();
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withDelimiter(';').withHeader("equipementns","designation","nb_panne","type","marque","date_fabrication","prix","depenses","code_station","existe");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Equipement equipement : equipements) {
                List<String> data = Arrays.asList(
                        equipement.getEquipementNS(),
                        equipement.getDesignation(),
                        String.valueOf(equipement.getNbPanne()),
                        equipement.getType(),
                        equipement.getMarque(),
                        equipement.getDateFabrication().toString().substring(0,10),
                        String.valueOf(equipement.getPrix()),
                        String.valueOf(equipement.getDepenses()),
                        equipement.getStation().getCodeStation(),
                        String.valueOf(equipement.isExiste())
                );
                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }

    }

    public void uploadPDRFile(MultipartFile file) {
        try {
            BufferedReader fileReader = new BufferedReader(newReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(fileReader,CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            List<PDR> pdrs = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                PDR pdr = new PDR(
                        csvRecord.get("marque"),
                        csvRecord.get("designation"),
                        Long.parseLong(csvRecord.get("prix"))
                );
                pdrs.add(pdr);
            }
            pdrRepository.saveAll(pdrs);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream exportPDRFile() {
        List<PDR> pdrs = pdrRepository.findAll();
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL).withDelimiter(';').withHeader("designation","marque","prix");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (PDR pdr : pdrs) {
                List<String> data = Arrays.asList(
                        pdr.getDesignation(),
                        pdr.getMarque(),
                        String.valueOf(pdr.getPrix())
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }


    }


}
