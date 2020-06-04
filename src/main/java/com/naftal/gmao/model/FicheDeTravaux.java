package com.naftal.gmao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

//@NamedEntityGraph(name = "FichTravauxGraph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "ordreDeTravail",subgraph = "OrdreGraph"),
//                @NamedAttributeNode(value = "station",subgraph = "stationChef")
//        },subgraphs = {
//        @NamedSubgraph(
//                name = "stationChef",
//                attributeNodes = {
//                        @NamedAttributeNode(value = "chefStation",subgraph = "UserRole")
//                }
//        ),@NamedSubgraph(
//        name = "OrdreGraph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "demandePDR",subgraph = "demandePDRGraph"),
//                @NamedAttributeNode(value = "demandeDeTravails",subgraph = "demandeDeTravailGraph")
//        }
//),@NamedSubgraph(name = "demandePDRGraph",
//        attributeNodes = {
//        @NamedAttributeNode(value = "cadre",subgraph = "UserRole"),
//        @NamedAttributeNode(value = "magasinier",subgraph = "UserRole")
//}
//),@NamedSubgraph(
//        name = "UserRole",
//        attributeNodes = {
//                @NamedAttributeNode(value = "roles")
//        }
//)
//        ,@NamedSubgraph(
//        name = "demandeDeTravailGraph",
//        attributeNodes = {
//                @NamedAttributeNode(value = "panne")
//        }
//)
////        ,@NamedSubgraph(
////        name = "panneGraph",
////        attributeNodes = {
////                @NamedAttributeNode(value = "composants")
////        }
////)
//}
//)
//

@NamedEntityGraph(name = "FichTravauxGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "ordreDeTravail",subgraph = "OrdreGraph"),
                @NamedAttributeNode(value = "station",subgraph = "stationChef")
        },subgraphs = {
        @NamedSubgraph(
                name = "stationChef",
                attributeNodes = {
                        @NamedAttributeNode(value = "chefStation",subgraph = "UserGraph")
                }
        ),@NamedSubgraph(
        name = "OrdreGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "demandeDeTravails",subgraph = "dtGraph"),
                @NamedAttributeNode(value = "demandePDR",subgraph = "dpGraph")

        }
),
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
)




}
)




public class FicheDeTravaux extends Document {

    @OneToOne
    @JoinColumn(name = "idOT")
    @Fetch(FetchMode.JOIN)
    private OrdreDeTravail ordreDeTravail;

    @ManyToOne
    @JoinColumn(name = "CodeStation")
    @Fetch(FetchMode.JOIN)
    private Station station;


    private int tempTravail=0;
    private int tempTrajet=0;
    private int tempTotal=0;


    private boolean valideChef=false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationChefdate;

    private boolean valideCadre=false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationCadredate;

    private boolean valideMagasinier=false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date validationMagasinierdate;

    @Lob
    private String description;

    private boolean rempli=false;
    @Temporal(TemporalType.TIMESTAMP)
    private Date remplissageIntervenantdate;

}
