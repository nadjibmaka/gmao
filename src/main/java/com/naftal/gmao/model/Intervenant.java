package com.naftal.gmao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Intervenant extends Utilisateur {


    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    private int nbHeurs;

    @Enumerated(EnumType.STRING)
    private Secteur secteur;

    @Column(columnDefinition = "boolean default true")
    private Boolean disponible;



}
