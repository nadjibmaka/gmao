package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@Data
public class Cadre extends  Utilisateur{




    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cadre")
    private  List<DemandePDR> demandePDRs;

    @JsonIgnore
    @OneToMany(mappedBy = "cadre" , fetch = FetchType.LAZY)
    private List<OrdreDeTravail> ordreDeTravails;


}
