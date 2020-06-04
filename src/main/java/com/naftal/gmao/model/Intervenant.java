package com.naftal.gmao.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)

@NamedEntityGraph(name = "intervenantGraph"
        ,attributeNodes = {
        @NamedAttributeNode(value = "roles"),
        @NamedAttributeNode(value = "nbHeurs")
}
)
public class Intervenant extends Utilisateur {


    @Enumerated(EnumType.STRING)
    private Specialite specialite;


    @OneToMany(mappedBy = "intervenant",cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<NbHeureLigne> nbHeurs = null;

    @Enumerated(EnumType.STRING)
    private Secteur secteur;

    @Column(columnDefinition = "boolean default true")
    private Boolean disponible;


}
