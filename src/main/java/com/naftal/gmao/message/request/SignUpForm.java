package com.naftal.gmao.message.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

public class SignUpForm {

    private String matricule;

    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    @NotBlank
    @Size(min=3, max = 50)
    private String prenom;

    private Date dateNaissance;

    @NotBlank
    private String numTelephone;


    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;
    
    private Set<String> role;

    private String specialite;

    private String secteur;

    private String codeStation;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;



    public String getCodeStation() {
        return codeStation;
    }

    public void setCodeStation(String codeStation) {
        this.codeStation = codeStation;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
    	return this.role;
    }

    public void setRole(Set<String> role) {
    	this.role = role;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }


    public SignUpForm(String matricule, @NotBlank @Size(min = 3, max = 50) String name, @NotBlank @Size(min = 3, max = 50) String prenom, Date dateNaissance, @NotBlank String numTelephone, @NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(max = 60) @Email String email, Set<String> role, @NotBlank @Size(min = 6, max = 40) String password) {
        this.matricule = matricule;
        this.name = name;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numTelephone = numTelephone;
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }


    public SignUpForm(String matricule, @NotBlank @Size(min = 3, max = 50) String name, @NotBlank @Size(min = 3, max = 50) String prenom, Date dateNaissance, @NotBlank String numTelephone, @NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(max = 60) @Email String email, Set<String> role, String specialite, String secteur, @NotBlank @Size(min = 6, max = 40) String password) {
        this.matricule = matricule;
        this.name = name;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numTelephone = numTelephone;
        this.username = username;
        this.email = email;
        this.role = role;
        this.specialite = specialite;
        this.secteur = secteur;
        this.password = password;
    }

    public SignUpForm(String matricule, @NotBlank @Size(min = 3, max = 50) String name, @NotBlank @Size(min = 3, max = 50) String prenom, Date dateNaissance, @NotBlank String numTelephone, @NotBlank @Size(min = 3, max = 50) String username, @NotBlank @Size(max = 60) @Email String email, Set<String> role, String codeStation, @NotBlank @Size(min = 6, max = 40) String password) {
        this.matricule = matricule;
        this.name = name;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numTelephone = numTelephone;
        this.username = username;
        this.email = email;
        this.role = role;
        this.codeStation = codeStation;
        this.password = password;
    }

    public SignUpForm() {
    }
}