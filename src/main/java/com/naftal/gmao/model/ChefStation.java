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
@NamedEntityGraph(name = "chefstationGraph"
        ,attributeNodes = {
        @NamedAttributeNode(value = "roles")
}
)


public class ChefStation extends Utilisateur{


    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "codeStation")
    private Station station;


    @JsonIgnore
    @OneToMany(mappedBy = "emetteur",fetch = FetchType.LAZY)
    private List<DemandeDeTravail> demandeDeTravails;




}
