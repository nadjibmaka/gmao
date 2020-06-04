package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NamedEntityGraph(name = "equipementGraph",
        attributeNodes = {
        @NamedAttributeNode(value = "station", subgraph = "station.chef"),
        @NamedAttributeNode(value = "composants")

        },
        subgraphs = {
        @NamedSubgraph(
                name = "station.chef",
                attributeNodes = {
                        @NamedAttributeNode(value = "chefStation",subgraph = "chefstationRole")
                }
        ),@NamedSubgraph(name = "chefstationRole",attributeNodes = @NamedAttributeNode("roles"))
        }
)

@NoArgsConstructor
@Data
@AllArgsConstructor

public class Equipement {

 @Id
 private String equipementNS;

 private String designation;

@Column(columnDefinition = "integer default 0")
 private int nbPanne = 0;

 @Nullable
 private String type;

 private String marque;

 @JsonFormat(pattern="yyyy-MM-dd")
 private Date dateFabrication;

 @JsonFormat(pattern="yyyy-MM-dd")
 private Date dateDernierePanne;


 private Long prix;

 @Column(columnDefinition = "integer default 0")
 private Long depenses = Long.valueOf(0);


 @OneToMany(mappedBy = "equipement",cascade = CascadeType.ALL)
 @Fetch(FetchMode.SUBSELECT)
    private List<Composant> composants;

    @JsonIgnore
    @OneToMany(mappedBy = "equipement",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Panne> pannes ;



    @ManyToOne
    @JoinColumn(name="codeStation")
    @Fetch(FetchMode.JOIN)
    private Station station;

    @Column(columnDefinition = "boolean default true")
    private boolean existe=true;

    public Equipement(String equipementNS, String designation, int nbPanne, @Nullable String type, String marque, Date dateFabrication, Long prix, Long depenses, Station station, boolean existe) {
        this.equipementNS = equipementNS;
        this.designation = designation;
        this.nbPanne = nbPanne;
        this.type = type;
        this.marque = marque;
        this.dateFabrication = dateFabrication;
        this.prix = prix;
        this.depenses = depenses;
        this.station = station;
        this.existe = existe;
    }
}
