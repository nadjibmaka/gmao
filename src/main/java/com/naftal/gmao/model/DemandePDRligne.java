package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private PDR pdr ;

    private int quantite ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="IDdemandePDR")
    private DemandePDR demandePDR;




}
