package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data

@NamedEntityGraph(
        name = "dpGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "cadre",subgraph = "UserGraph"),
                @NamedAttributeNode(value = "magasinier",subgraph = "UserGraph"),
                @NamedAttributeNode(value = "ordreDeTravail",subgraph = "otGraph")

        },subgraphs = {
        @NamedSubgraph(
        name = "UserGraph",
        attributeNodes = {
                @NamedAttributeNode("roles")
        }
),@NamedSubgraph(
        name = "otGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "demandeDeTravails",subgraph = "dtGraph"),
                @NamedAttributeNode(value = "ficheDeTravaux",subgraph = "ftGraph")
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
        name = "ftGraph",
        attributeNodes = {
                @NamedAttributeNode(value = "station",subgraph = "stationGraph")
        }
)
}
)



public class DemandePDR extends Document {

    @ManyToOne
    @JoinColumn(name = "matriculeCadre")
    @Fetch(FetchMode.JOIN)
    private Cadre cadre;

    @ManyToOne
    @JoinColumn(name = "matriculeMagasinier")
    @Fetch(FetchMode.JOIN)
    private Magasinier magasinier;

    @OneToMany(mappedBy = "demandePDR",cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandePDRligne> demandePDRlignes;

    @JsonIgnore
    @OneToOne(mappedBy = "demandePDR",fetch = FetchType.LAZY)
    private OrdreDeTravail ordreDeTravail;

}
