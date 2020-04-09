package com.naftal.gmao.repository;

import com.naftal.gmao.model.OrdreDeTravail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdreRepository extends JpaRepository<OrdreDeTravail, Long> {
    List<OrdreDeTravail> findAllByCadre_UsernameOrderByDateDesc(String username);
    List<OrdreDeTravail> findAllByIntervenants_UsernameAndTraite(String username,Boolean traite);
}
