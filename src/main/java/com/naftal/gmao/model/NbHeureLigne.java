package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NbHeureLigne {

    @Id
    @GeneratedValue
    private Long idLigne;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="matriculeInter")
    private Intervenant intervenant ;

    private String date;

    private int nbHeure;

    public NbHeureLigne(String date, int nbHeure,Intervenant inter) {
        this.date = date;
        this.nbHeure = nbHeure;
        this.intervenant=inter;
    }
}
