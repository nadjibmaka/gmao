package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import com.naftal.gmao.model.Equipement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, String> {

    @EntityGraph(value = "equipementGraph")
    List<Equipement> findByStationCodeStationOrderByEquipementNS(String codeStation);

    @EntityGraph(value = "equipementGraph")
    List<Equipement> findByExisteOrderByEquipementNS(boolean existe);

    @EntityGraph(value = "equipementGraph")
    Equipement findByEquipementNS(String equipementNS);

}
