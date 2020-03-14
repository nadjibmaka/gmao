package com.naftal.gmao.repository;

import com.naftal.gmao.model.Composant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface ComposantRepositoty extends JpaRepository<Composant, String> {
}
