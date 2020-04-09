package com.naftal.gmao.repository;

import com.naftal.gmao.model.DemandePDR;
import com.naftal.gmao.model.DemandePDRligne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DemandePDRLigneRepository extends JpaRepository<DemandePDRligne, Long> {
    @Transactional
    Long deleteAllByDemandePDR(DemandePDR demandePDR);
}
