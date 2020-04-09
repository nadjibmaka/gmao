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
public class OrdreDeTravail  extends  Document{

    private String instruction;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "matriculeCadre")
    @Fetch(FetchMode.JOIN)
    private Cadre cadre;

    @OneToMany(mappedBy = "ordreDeTravail" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandeDeTravail> demandeDeTravails;

    @OneToOne(mappedBy = "ordreDeTravail")
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private FicheDeTravaux ficheDeTravaux;

    @ManyToMany(fetch = FetchType.LAZY)
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
