package com.naftal.gmao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@NamedEntityGraph(name = "FichTravauxGraph",
        attributeNodes = {@NamedAttributeNode("ordreDeTravail"),@NamedAttributeNode("station")}
)

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
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
    private boolean valideCadre=false;
    private boolean valideMagasinier=false;
    private String description;
    private boolean rempli=false;

}
