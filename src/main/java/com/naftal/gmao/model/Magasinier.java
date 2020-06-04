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
@Data
@NamedEntityGraph(name = "magasinierGraph"
        ,attributeNodes = {
        @NamedAttributeNode(value = "roles")
}
)
public class Magasinier extends Utilisateur {

    @OneToMany(mappedBy = "magasinier",fetch = FetchType.LAZY)
    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandePDR> demandePDRs;




}
