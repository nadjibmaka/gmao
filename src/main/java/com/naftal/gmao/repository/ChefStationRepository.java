package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChefStationRepository extends JpaRepository<ChefStation, Long> {


    @EntityGraph(value = "chefStationGraph")
    ChefStation findByMatricule(String matricule);
    ChefStation findByUsername(String username);
}
