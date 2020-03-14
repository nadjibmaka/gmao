package com.naftal.gmao.repository;

import com.naftal.gmao.model.Commercial;
import com.naftal.gmao.model.Magasinier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommercialRepository extends JpaRepository<Commercial, Long> {
}
