package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PDR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPDR;

    private String marque;

    private String designation;

    private Long prix ;

    @JsonIgnore
    @OneToMany(mappedBy = "pdr",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandePDRligne> demandePDRlignes;

    public PDR(String marque, String designation, Long prix) {
        this.marque = marque;
        this.designation = designation;
        this.prix = prix;
    }
}
