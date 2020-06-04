package com.naftal.gmao.repository;

import com.naftal.gmao.model.FicheDeTravaux;
import com.naftal.gmao.model.Magasinier;
import com.naftal.gmao.model.OrdreDeTravail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FicheRepository extends JpaRepository<FicheDeTravaux, Long> {
    @EntityGraph(value = "FichTravauxGraph")
    FicheDeTravaux findByOrdreDeTravailAndRempli(OrdreDeTravail ordreDeTravail,Boolean rempli);
    @EntityGraph(value = "FichTravauxGraph")
    List<FicheDeTravaux> findAllByStation_ChefStation_UsernameAndValideChefAndRempliOrderByDateDesc(String username,boolean valide,boolean rempli);
    @EntityGraph(value = "FichTravauxGraph")
    List<FicheDeTravaux> findAllByValideChefAndRempliOrderByDateDesc(boolean valide,boolean rempli);
    @EntityGraph(value = "FichTravauxGraph")
    List<FicheDeTravaux>findAllByOrdreDeTravail_DemandePDR_Magasinier_UsernameAndOrdreDeTravail_DemandePDR_TraiteOrderByDateDesc(String username,boolean traite);
    @EntityGraph(value = "FichTravauxGraph")
    List<FicheDeTravaux> findAllByOrdreDeTravail_Cadre_UsernameAndValideCadreOrderByDateDesc(String username,boolean valid);


}
