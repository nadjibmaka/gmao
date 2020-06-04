package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

//@NamedEntityGraph(name = "OrdreGraph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "demandePDR",subgraph = "demandePDRGraph"),
//                @NamedAttributeNode(value = "demandeDeTravails",subgraph = "demandeDeTravailGraph"),
//                @NamedAttributeNode(value = "ficheDeTravaux")
//
//        },subgraphs = {
//        @NamedSubgraph(name = "demandePDRGraph",
//                attributeNodes = {
//                        @NamedAttributeNode(value = "cadre",subgraph = "UserRole"),
//                        @NamedAttributeNode(value = "magasinier",subgraph = "UserRole")
//                }
//
//        ),@NamedSubgraph(
//        name = "demandeDeTravailGraph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "panne",subgraph = "panneEquipements")
//        }
//),@NamedSubgraph(name = "panneEquipements",
//        attributeNodes = {
//                @NamedAttributeNode(value = "equipement")
//        })
//}
//)

@NamedEntityGraph(
        name = "OrdreGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "demandeDeTravails",subgraph = "dtGraph"),
                @NamedAttributeNode(value = "demandePDR",subgraph = "dpGraph"),
                @NamedAttributeNode(value = "ficheDeTravaux",subgraph = "ftGraph"),
        },subgraphs = {
@NamedSubgraph(
                name = "dpGraph",
                attributeNodes = {
                        @NamedAttributeNode(value = "cadre",subgraph = "UserGraph"),
                        @NamedAttributeNode(value = "magasinier",subgraph = "UserGraph")
                                    }
),@NamedSubgraph(
        name = "dtGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "panne",subgraph = "panneGraph"),
                @NamedAttributeNode(value = "emetteur",subgraph = "UserGraph")
        }
),@NamedSubgraph(
        name = "panneGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "equipement",subgraph = "equipementGraph")
        }
),@NamedSubgraph(
        name = "equipementGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "station",subgraph = "stationGraph")
        }
),@NamedSubgraph(
        name = "stationGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "chefStation",subgraph = "UserGraph")
        }
),@NamedSubgraph(
        name = "UserGraph",
        attributeNodes = {
                @NamedAttributeNode("roles")
        }
),@NamedSubgraph(
        name = "ftGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "station",subgraph = "stationGraph")
        }
)
}
)




public class OrdreDeTravail  extends  Document{


    @Lob
    private String instruction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "matriculeCadre")
    private Cadre cadre;

    @OneToMany(mappedBy = "ordreDeTravail")
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandeDeTravail> demandeDeTravails;

    @OneToOne(mappedBy = "ordreDeTravail",fetch = FetchType.LAZY)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private FicheDeTravaux ficheDeTravaux;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "intervenants_ordreDeTravails",
            joinColumns = @JoinColumn(name = "idDocument"),
            inverseJoinColumns = @JoinColumn(name = "matricule"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Intervenant> intervenants  = new ArrayList<>();


    @OneToOne
    @JoinColumn(name = "idDemandePDR")
    @Fetch(FetchMode.JOIN)
    private DemandePDR demandePDR;

    private int tempTravailEstime;

    private int tempTrajetEstime;


}
