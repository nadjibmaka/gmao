package com.naftal.gmao.repository;

import com.naftal.gmao.model.OrdreDeTravail;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdreRepository extends JpaRepository<OrdreDeTravail, Long> {


    @EntityGraph(value = "OrdreGraph")
    List<OrdreDeTravail> findAllByCadre_UsernameOrderByDateDesc(String username);

    @EntityGraph(value = "OrdreGraph")
    List<OrdreDeTravail> findAllByIntervenants_UsernameAndTraiteOrderByDateDesc(String username,Boolean traite);
}
