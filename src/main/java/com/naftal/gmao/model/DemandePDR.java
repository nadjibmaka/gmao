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
public class DemandePDR extends Document {

    @ManyToOne
    @JoinColumn(name = "matriculeCadre")
    @Fetch(FetchMode.JOIN)
    private Cadre cadre;

    @ManyToOne
    @JoinColumn(name = "matriculeMagasinier")
    @Fetch(FetchMode.JOIN)
    private Magasinier magasinier;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "demandePDR",cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandePDRligne> demandePDRlignes;

    @JsonIgnore
    @OneToOne(mappedBy = "demandePDR")
    @Fetch(FetchMode.JOIN)
    private OrdreDeTravail ordreDeTravail;

}
