package com.naftal.gmao.services;

import com.naftal.gmao.model.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.Desktop;

@Service
public class jasperReports {


    public String exportEmployeReport(List<Utilisateur> users) throws IOException, JRException {
    String desktopPath = System.getProperty("user.home")+"\\Desktop";
    File folder = new File(desktopPath+"\\GmaoReports");
    folder.mkdir();
    File file = ResourceUtils.getFile("classpath:employees.jrxml");
    JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);
    Map<String,Object> parameters = new HashMap<>();
    parameters.put("created by","naftal gmao");
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy,HH_mm_ss");
    String dateString = format.format( new Date()   );
    String destination = desktopPath+"\\GmaoReports\\utilisateurs"+dateString+".pdf";
    JasperExportManager.exportReportToPdfFile(jasperPrint,destination);
    Desktop desktop = Desktop.getDesktop();
    File dirToOpen = new File(destination);
    desktop.open(dirToOpen);
    return "fichier crée dans " + desktopPath + "\\GmaoReports";
}

    public byte[] exportFicheTravauxReport(List<FicheDeTravaux> ficheDeTravaux) throws IOException, JRException {
        File file = ResourceUtils.getFile("classpath:ficheTrav.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
     JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ficheDeTravaux);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("numeroFiche",ficheDeTravaux.get(0).getIdDocument());
        parameters.put("station",ficheDeTravaux.get(0).getStation().getRaisonSocial());
        parameters.put("tempTravailEstime",ficheDeTravaux.get(0).getOrdreDeTravail().getTempTravailEstime());
        parameters.put("tempTrajetEstime",ficheDeTravaux.get(0).getOrdreDeTravail().getTempTrajetEstime());
        parameters.put("tempTotalEstime",ficheDeTravaux.get(0).getOrdreDeTravail().getTempTrajetEstime()+ficheDeTravaux.get(0).getOrdreDeTravail().getTempTravailEstime());
        parameters.put("tempTravailMentionne",ficheDeTravaux.get(0).getTempTravail());
        parameters.put("tempTrajetMentionne",ficheDeTravaux.get(0).getTempTrajet());
        parameters.put("tempTotalMentionne",ficheDeTravaux.get(0).getTempTotal());
        parameters.put("description",ficheDeTravaux.get(0).getDescription());
        parameters.put("instructions",ficheDeTravaux.get(0).getOrdreDeTravail().getInstruction());

        parameters.put("intervenantsList",ficheDeTravaux.get(0).getOrdreDeTravail().getIntervenants());

        List<Equipement> equipements = new ArrayList<>();
        for(DemandeDeTravail demandeDeTravail: ficheDeTravaux.get(0).getOrdreDeTravail().getDemandeDeTravails()){
            if (demandeDeTravail.getPanne().getEquipement() != null)
            equipements.add(demandeDeTravail.getPanne().getEquipement());
        }
        parameters.put("equipementsList",equipements);


        List<Composant> composants = new ArrayList<>();
        for(DemandeDeTravail demandeDeTravail: ficheDeTravaux.get(0).getOrdreDeTravail().getDemandeDeTravails()){
           if (demandeDeTravail.getPanne().getComposants() != null)
            composants.addAll(demandeDeTravail.getPanne().getComposants());
        }
        parameters.put("composantsList",composants);


        if (ficheDeTravaux.get(0).getOrdreDeTravail().getDemandePDR() != null)
        parameters.put("pdrList",ficheDeTravaux.get(0).getOrdreDeTravail().getDemandePDR().getDemandePDRlignes());



        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);

    }

    public byte[] exportOrdreReport(List<OrdreDeTravail> ordres) throws IOException, JRException {
//        String desktopPath = System.getProperty("user.home")+"\\Desktop";
//        File folder = new File(desktopPath+"\\GmaoReports");
//        folder.mkdir();
        File file = ResourceUtils.getFile("classpath:Ordre.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ordres);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("numeroOrdre",ordres.get(0).getIdDocument());
        parameters.put("station",ordres.get(0).getDemandeDeTravails().get(0).getPanne().getEquipement().getStation().getRaisonSocial());
        parameters.put("tempTravailEstime",ordres.get(0).getTempTravailEstime());
        parameters.put("tempTrajetEstime",ordres.get(0).getTempTrajetEstime());
        parameters.put("tempTotalEstime",ordres.get(0).getTempTrajetEstime()+ordres.get(0).getTempTravailEstime());
        parameters.put("instructions",ordres.get(0).getInstruction());
        parameters.put("intervenantsList",ordres.get(0).getIntervenants());
        parameters.put("date",ordres.get(0).getDate().toString().substring(0,19));

        List<Equipement> equipements = new ArrayList<>();
        for(DemandeDeTravail demandeDeTravail: ordres.get(0).getDemandeDeTravails()){
            if (demandeDeTravail.getPanne().getEquipement() != null)
                equipements.add(demandeDeTravail.getPanne().getEquipement());
        }
        parameters.put("equipementsList",equipements);


        List<Composant> composants = new ArrayList<>();
        for(DemandeDeTravail demandeDeTravail: ordres.get(0).getDemandeDeTravails()){
            if (demandeDeTravail.getPanne().getComposants() != null)
                composants.addAll(demandeDeTravail.getPanne().getComposants());
        }
        parameters.put("composantsList",composants);


        if (ordres.get(0).getDemandePDR() != null)
            parameters.put("pdrList",ordres.get(0).getDemandePDR().getDemandePDRlignes());



        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
//        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy,HH_mm_ss");
//        String dateString = format.format( new Date()   );
//        String destination = desktopPath+"\\GmaoReports\\OrdreDeTravail"+dateString+".pdf";
        return JasperExportManager.exportReportToPdf(jasperPrint);
//        Desktop desktop = Desktop.getDesktop();
//        File dirToOpen = new File(destination);
//        desktop.open(dirToOpen);
//        return "fichier crée dans " + desktopPath + "\\GmaoReports";

    }

    public byte[] exportDemandeReport(List<DemandeDeTravail> demandes) throws IOException, JRException {
        File file = ResourceUtils.getFile("classpath:Demande.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(demandes);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("numeroDemande",demandes.get(0).getIdDocument());
        parameters.put("station",demandes.get(0).getPanne().getEquipement().getStation().getRaisonSocial());
        parameters.put("description",demandes.get(0).getPanne().getDescription());
        parameters.put("equipement",demandes.get(0).getPanne().getEquipement().getDesignation());
        parameters.put("date",demandes.get(0).getDate().toString().substring(0,19));



        List<Composant> composants = new ArrayList<>();
            if (demandes.get(0).getPanne().getComposants() != null)
                composants.addAll(demandes.get(0).getPanne().getComposants());
        parameters.put("composantsList",composants);




        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportDemandePDRReport(List<DemandePDR> demandes) throws IOException, JRException {
        File file = ResourceUtils.getFile("classpath:DemandePDR.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(demandes);

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("numeroDemandePDR",demandes.get(0).getIdDocument());
        parameters.put("demandeur",demandes.get(0).getCadre().getName()+" "+demandes.get(0).getCadre().getPrenom());
        parameters.put("date",demandes.get(0).getDate().toString().substring(0,19));

        if (demandes.get(0).getDemandePDRlignes() != null)
            parameters.put("pdrList",demandes.get(0).getDemandePDRlignes());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }



}
