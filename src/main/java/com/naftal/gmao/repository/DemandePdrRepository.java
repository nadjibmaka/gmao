package com.naftal.gmao.repository;

import com.naftal.gmao.model.DemandePDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DemandePdrRepository extends JpaRepository<DemandePDR, Long> {
}
