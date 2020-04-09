package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NamedEntityGraph(name = "Station.chefStation",
        attributeNodes = @NamedAttributeNode("chefStation")
)
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


    @OneToOne(fetch = FetchType.LAZY,mappedBy = "station")
    @Fetch(FetchMode.JOIN)
    private ChefStation chefStation;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station",cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Equipement> equipements;


    @Column(columnDefinition = "boolean default true")
    private boolean existe=true;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station")
    @Fetch(FetchMode.SUBSELECT)
    private List<FicheDeTravaux> ficheDeTravaux;


}
