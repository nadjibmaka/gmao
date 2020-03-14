package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DemandeDeTravail extends Document {


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "idOrdre")
    private OrdreDeTravail ordreDeTravail;

    @ManyToOne
    @JoinColumn(name = "matriculeUtilisateur")
    private Utilisateur emetteur;

    @OneToOne
    @JoinColumn(name = "idPanne")
    private Panne panne;


}
