package com.naftal.gmao.repository;

import com.naftal.gmao.model.Utilisateur;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Long> {


    @Override
//    @EntityGraph(value = "utilisateurRole")
    List<Utilisateur> findAll();


//    @EntityGraph(value = "utilisateurRole")
    Optional<Utilisateur> findByUsername(String username);

//    @EntityGraph(value = "utilisateurRole")
    Utilisateur findByMatricule(String matricule);
//    @EntityGraph(value = "utilisateurRole")
    Boolean existsByUsername(String username);
//    @EntityGraph(value = "utilisateurRole")
    Boolean existsByEmail(String email);


   Long deleteByMatricule(String matricule);
}