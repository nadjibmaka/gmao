package com.naftal.gmao.model;

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
public class Magasinier extends Utilisateur {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "magasinier")
    private List<DemandePDR> demandePDRs;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "magasinier")
    private List<FicheDeTravaux>ficheDeTravauxs;

}
