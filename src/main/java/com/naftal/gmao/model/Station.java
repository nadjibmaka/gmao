package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Station {

    @Id
    private String codeStation;

    private String raisonSocial;

    private Long distance;

    private Long tempTrajet;

    private String district = "District Commercial Chlef";

    @Enumerated(EnumType.STRING)
    private Secteur secteur;

    private String adresse;

    @Enumerated(EnumType.STRING)
    private TypeStation typeStation;


    @OneToOne(mappedBy = "station")
    private ChefStation chefStation;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station",cascade = CascadeType.ALL)
    private List<Equipement> equipements;


    @Column(columnDefinition = "boolean default true")
    private boolean existe=true;



}
