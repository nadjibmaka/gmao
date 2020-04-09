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
        attributeNodes = {@NamedAttributeNode("ordreDeTravail"),@NamedAttributeNode("panne"),@NamedAttributeNode("emetteur")}
)

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DemandeDeTravail extends Document {


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "idOrdre")
    @Fetch(FetchMode.JOIN)
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
