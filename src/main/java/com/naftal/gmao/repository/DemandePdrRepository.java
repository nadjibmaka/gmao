package com.naftal.gmao.repository;

import com.naftal.gmao.model.DemandePDR;
import com.naftal.gmao.model.Magasinier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandePdrRepository extends JpaRepository<DemandePDR, Long> {
    List<DemandePDR>findAllByMagasinierAndAndOrdreDeTravail_Traite(Magasinier magasinier, boolean traiteOrdre);
    List<DemandePDR>findAllByMagasinier_UsernameAndTraite(String username,boolean traite);



}
