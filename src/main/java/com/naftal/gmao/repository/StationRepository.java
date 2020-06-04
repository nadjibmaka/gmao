package com.naftal.gmao.repository;

import com.naftal.gmao.model.ChefStation;
import com.naftal.gmao.model.Station;
import com.naftal.gmao.model.TypeStation;
import com.naftal.gmao.model.Utilisateur;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface StationRepository extends JpaRepository<Station, String> {






 @EntityGraph(value = "Station.chefStation")
   List<Station> findAllByOrderByCodeStationAsc();
    @EntityGraph(value = "Station.chefStation")
    Station findByCodeStation(String codeStation);
    @EntityGraph(value = "Station.chefStation")
    List<Station>findAllByExiste(boolean existe);
    @EntityGraph(value = "Station.chefStation")
    List<Station>findAllByChefStationAndExiste(ChefStation chefStation,boolean existe);
    @EntityGraph(value = "Station.chefStation")
    List<Station>findAllByTypeStation(TypeStation type);
    @EntityGraph(value = "Station.chefStation")
    Station findByChefStation_Username(String username);

    @EntityGraph(value = "Station.chefStation")
    @Query("SELECT distinct s FROM Station s join Equipement e on e.station=s join Panne p on p.equipement=e join DemandeDeTravail dt on dt.panne=p where dt.traite=0")
    List<Station>findByDemande();
}
