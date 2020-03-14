package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefStationRepository extends JpaRepository<ChefStation, Long> {
    ChefStation findByMatricule(String matricule);
    ChefStation findByUsername(String username);
}
