package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ChefStation extends Utilisateur{


    @OneToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "codeStation")
    @Fetch(FetchMode.JOIN)
    private Station station;


    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emetteur")
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandeDeTravail> demandeDeTravails;




}
