package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class Commercial extends Utilisateur {

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "emetteur")
    @Fetch(FetchMode.SUBSELECT)
    private List<DemandeDeTravail> demandeDeTravails;

}
