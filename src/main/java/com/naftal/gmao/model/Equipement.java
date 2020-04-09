package com.naftal.gmao.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor

public class Equipement {

 @Id
 private String equipementNS;

 private String designation;

@Column(columnDefinition = "integer default 0")
 private int nbPanne = 0;

 @Nullable
 private String type;

 private String marque;

 @JsonFormat(pattern="yyyy-MM-dd")
 private Date dateFabrication;

 private Long prix;

 @Column(columnDefinition = "integer default 0")
 private Long depenses = Long.valueOf(0);


 @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipement",cascade = CascadeType.ALL)
 @Fetch(FetchMode.SUBSELECT)
    private List<Composant> composants;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipement",cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Panne> pannes ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="codeStation")
    @Fetch(FetchMode.JOIN)
    private Station station;

    @Column(columnDefinition = "boolean default true")
    private boolean existe=true;

}
