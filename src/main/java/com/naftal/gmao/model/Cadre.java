package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NamedEntityGraph(name = "cadreGraph"
        ,attributeNodes = {
        @NamedAttributeNode(value = "roles")
}
)
@Data
public class Cadre extends  Utilisateur{




    @JsonIgnore
    @OneToMany( mappedBy = "cadre",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private  List<DemandePDR> demandePDRs;

    @JsonIgnore
    @OneToMany(mappedBy = "cadre",fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<OrdreDeTravail> ordreDeTravails;


}
