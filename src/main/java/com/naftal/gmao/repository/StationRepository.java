package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import com.naftal.gmao.model.Station;
import com.naftal.gmao.model.TypeStation;
import com.naftal.gmao.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface StationRepository extends JpaRepository<Station, String> {
    Station findByCodeStation(String codeStation);
    List<Station>findAllByExiste(boolean existe);
    List<Station>findAllByChefStationAndExiste(ChefStation chefStation,boolean existe);
    List<Station>findAllByTypeStation(TypeStation type);
    Station findByChefStation_Username(String username);


    @Query("SELECT distinct s FROM Station s join Equipement e on e.station=s join Panne p on p.equipement=e join DemandeDeTravail dt on dt.panne=p where dt.traite=0")
    List<Station>findByDemande();
}
