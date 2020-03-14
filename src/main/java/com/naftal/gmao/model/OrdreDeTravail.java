package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private Cadre cadre;

    @OneToMany(mappedBy = "ordreDeTravail" , fetch = FetchType.LAZY)
    private List<DemandeDeTravail> demandeDeTravails;

    @OneToOne(mappedBy = "ordreDeTravail")
    private FicheDeTravaux ficheDeTravaux;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "intervenants_ordreDeTravails",
            joinColumns = @JoinColumn(name = "idDocument"),
            inverseJoinColumns = @JoinColumn(name = "matricule"))
    private List<Intervenant> intervenants  = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idDemandePDR")
    private DemandePDR demandePDR;

    private int tempTravailEstime;

    private int tempTrajetEstime;


}
