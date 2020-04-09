package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Panne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPanne;

    private String description;


    @Enumerated(EnumType.STRING)
    private TypePanne typePanne;



   @ManyToOne
   @JoinColumn(name="equipementNS")
   @Fetch(FetchMode.JOIN)
   private Equipement equipement;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pannes_composants",
            joinColumns = @JoinColumn(name = "idPanne"),
            inverseJoinColumns = @JoinColumn(name = "cnumS"))
    @Fetch(FetchMode.SUBSELECT)
    private List<Composant> composants = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "panne")
    @Fetch(FetchMode.JOIN)
    private DemandeDeTravail demandeDeTravail;

}
