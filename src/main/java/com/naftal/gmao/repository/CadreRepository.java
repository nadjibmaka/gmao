package com.naftal.gmao.repository;

import com.naftal.gmao.model.Cadre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadreRepository extends JpaRepository<Cadre, Long> {
    Cadre findByUsername(String username);
}
