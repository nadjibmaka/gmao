package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity


@NamedEntityGraph(name = "DemandeDeTravailGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "panne",subgraph = "panneGraph"),
                @NamedAttributeNode(value = "emetteur",subgraph = "UserGraph")
        },subgraphs = {
        @NamedSubgraph(
                name = "panneGraph",
                attributeNodes = {
                        @NamedAttributeNode(value = "equipement",subgraph = "equipementGraph"),
                        @NamedAttributeNode(value = "composants")
                }
        ),
        @NamedSubgraph(
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


@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DemandeDeTravail extends Document {


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "idOrdre")
    private OrdreDeTravail ordreDeTravail;

    @ManyToOne
    @JoinColumn(name = "matriculeUtilisateur")
    @Fetch(FetchMode.JOIN)
    private Utilisateur emetteur;

    @OneToOne
    @JoinColumn(name = "idPanne")
    @Fetch(FetchMode.JOIN)
    private Panne panne;


}
