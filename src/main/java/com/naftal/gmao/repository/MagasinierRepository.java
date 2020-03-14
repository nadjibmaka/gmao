package com.naftal.gmao.repository;

import com.naftal.gmao.model.Magasinier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagasinierRepository extends JpaRepository<Magasinier, Long> {
}
