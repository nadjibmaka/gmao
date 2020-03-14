package com.naftal.gmao.message.request;

import com.naftal.gmao.model.Secteur;
import com.naftal.gmao.model.TypeStation;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

public class EditStationForm {

    @Id
    private String codeStation;

    private String raisonSocial;

    private Long distance;

    private Long tempTrajet;

    private String district;

    private String secteur;

    private String adresse;


    private String typeStation;

    public String getCodeStation() {
        return codeStation;
    }

    public void setCodeStation(String codeStation) {
        this.codeStation = codeStation;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Long getTempTrajet() {
        return tempTrajet;
    }

    public void setTempTrajet(Long tempTrajet) {
        this.tempTrajet = tempTrajet;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTypeStation() {
        return typeStation;
    }

    public void setTypeStation(String typeStation) {
        this.typeStation = typeStation;
    }
}
