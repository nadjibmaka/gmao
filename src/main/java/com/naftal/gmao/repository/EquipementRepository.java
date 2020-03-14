package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import com.naftal.gmao.model.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, String> {
    List<Equipement> findByStationCodeStation(String codeStation);
    List<Equipement> findByExiste(boolean existe);
    Equipement findByEquipementNS(String equipementNS);

}
