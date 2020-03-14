package com.naftal.gmao.repository;


import com.naftal.gmao.model.PDR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PdrRepository extends JpaRepository<PDR, Long> {
    PDR findByIdPDR(Long idPdr);
}
