package com.naftal.gmao.repository;

import com.naftal.gmao.model.DemandeDeTravail;
import com.naftal.gmao.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<DemandeDeTravail, Long> {
    List<DemandeDeTravail> findAllByEmetteur_Username(String username);
    List<DemandeDeTravail>findAllByTraite(boolean traite);
    DemandeDeTravail findByIdDocument(Long idDocument);
List<DemandeDeTravail> findByPanne_Equipement_Station_CodeStationAndTraite(String codeStation,Boolean traite);

//    @Query("SELECT distinct  dt FROM Station s join Panne p on p.station=s join Document d on p=d.panne join DemandeDeTravail dt on dt.idDocument=d.idDocument where d.traite=0 and s.codeStation=:codeStation")
//    List<DemandeDeTravail>findByStation(String codeStation);


}
