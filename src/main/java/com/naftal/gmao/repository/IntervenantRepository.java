package com.naftal.gmao.repository;

import com.naftal.gmao.model.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervenantRepository extends JpaRepository<Intervenant,Long> {
    Intervenant findByMatricule(String matricule);
}
