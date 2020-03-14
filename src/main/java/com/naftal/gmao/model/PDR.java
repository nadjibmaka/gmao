package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PDR {
    @Id
    private Long idPDR;

    private String marque;

    private String designation;

    private Long prix ;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pdr")
    private List<DemandePDRligne> demandePDRlignes;


}
