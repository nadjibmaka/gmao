package com.naftal.gmao.repository;

import com.naftal.gmao.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByUsername(String username);
    Utilisateur findByMatricule(String matricule);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);


   Long deleteByMatricule(String matricule);
}