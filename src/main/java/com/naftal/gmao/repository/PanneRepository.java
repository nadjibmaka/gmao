package com.naftal.gmao.repository;

import com.naftal.gmao.model.Panne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanneRepository extends JpaRepository<Panne, Long> {

}
