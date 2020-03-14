package com.naftal.gmao.repository;

import com.naftal.gmao.model.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Administrateur, Long> {
}
