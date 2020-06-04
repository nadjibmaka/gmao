package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DemandePDRligne {
    @Id
    @GeneratedValue
    private Long idLigne;

    @ManyToOne
    @JoinColumn(name="IdPDR")
    @Fetch(FetchMode.JOIN)
    private PDR pdr ;

    private int quantiteDemande;
    private int quantiteAccorde;
    private int quantiteUtilise;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="IDdemandePDR")
    private DemandePDR demandePDR;




}
