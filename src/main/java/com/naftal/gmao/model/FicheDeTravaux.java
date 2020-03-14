package com.naftal.gmao.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class FicheDeTravaux extends Document {

    @OneToOne
    @JoinColumn(name = "idOT")
    private OrdreDeTravail ordreDeTravail;

    @ManyToOne
    @JoinColumn(name = "matriculeChefStation")
    private ChefStation chefStation;

    @ManyToOne
    @JoinColumn(name = "matriculeMagasinier")
    private Magasinier magasinier;

    private int tempTravail=0;
    private int tempTrajet=0;
    private int tempTotal=0;

    private boolean valideChef=false;
    private boolean valideCadre=false;




}
