package com.naftal.gmao.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Composant {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private  Long composantNS;

@Enumerated(EnumType.STRING)
private UniteMesure unitemesure;

private String designation;


private String type;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="equipementNS")
@JsonIgnore
private Equipement equipement;

}
