package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private List<DemandeDeTravail> demandeDeTravails;

}
