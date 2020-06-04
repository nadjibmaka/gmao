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
        attributeNodes = @NamedAttributeNode(value = "chefStation",subgraph = "roleGraphe"),
        subgraphs = {
        @NamedSubgraph(
                name = "roleGraphe",
                attributeNodes = {
                        @NamedAttributeNode("roles")
                }
        )
        }
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


    @OneToOne(mappedBy = "station")
    @Fetch(FetchMode.JOIN)
    private ChefStation chefStation;

    @JsonIgnore
    @OneToMany(mappedBy = "station",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Equipement> equipements;

    @Column(columnDefinition = "boolean default true")
    private boolean existe=true;

    @JsonIgnore
    @OneToMany(mappedBy = "station",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<FicheDeTravaux> ficheDeTravaux;

    private Double longitude;
    private Double latitude;

    public Station(String codeStation, String raisonSocial, Long distance, Long tempTrajet, String district, String secteur, String adresse, String typeStation, boolean existe, Double longitude, Double latitude) {
        this.codeStation = codeStation;
        this.raisonSocial = raisonSocial;
        this.distance = distance;
        this.tempTrajet = tempTrajet;
        this.district = district;
if (secteur.equals("Tiaret")) this.secteur = Secteur.Tiaret; else if (secteur.equals("Ghelizane")) this.secteur = Secteur.Ghelizane; else if (secteur.equals("Chlef")) this.secteur = Secteur.Chlef;
        this.adresse = adresse;
if (typeStation.equals("GD")) this.typeStation = TypeStation.GD; else if (typeStation.equals("GL")) this.typeStation = TypeStation.GL; else if (typeStation.equals("PVA")) this.typeStation = TypeStation.PVA;
        this.existe = existe;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
