package com.naftal.gmao.repository;

import com.naftal.gmao.model.DemandePDR;
import com.naftal.gmao.model.Magasinier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandePdrRepository extends JpaRepository<DemandePDR, Long> {
    @EntityGraph(value = "dpGraph")
    List<DemandePDR>findAllByMagasinierAndAndOrdreDeTravail_TraiteOrderByDateDesc(Magasinier magasinier, boolean traiteOrdre);
    @EntityGraph(value = "dpGraph")
    List<DemandePDR>findAllByMagasinier_UsernameAndTraiteOrderByDateDesc(String username,boolean traite);



}
