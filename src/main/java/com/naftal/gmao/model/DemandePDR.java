package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class DemandePDR extends Document {

    @ManyToOne
    @JoinColumn(name = "matriculeCadre")
    private Cadre cadre;


//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "demandePDRs_PDRs",
//            joinColumns = @JoinColumn(name = "idDocument"),
//            inverseJoinColumns = @JoinColumn(name = "idPDR"))
//    private Set<PDR> PDRs = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "matriculeMagasinier")
    private Magasinier magasinier;

//    @ElementCollection
//    @CollectionTable(name = "PDR_Quantite")
//    @MapKeyJoinColumn(name = "idPDR")
//    @Column(name = "Quantite")
//    private Map<PDR, Integer> quantites= new HashMap<>();



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "demandePDR",cascade = CascadeType.ALL)
    private List<DemandePDRligne> demandePDRlignes;


    @JsonIgnore
    @OneToOne(mappedBy = "demandePDR")
    private OrdreDeTravail ordreDeTravail;

}
