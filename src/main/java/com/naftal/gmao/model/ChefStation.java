package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ChefStation extends Utilisateur{


    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "codeStation")
    private Station station;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chefStation")
    private List<FicheDeTravaux> ficheDeTravaux;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emetteur")
    private List<DemandeDeTravail> demandeDeTravails;




}
