package com.example.principal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.principal.model.Contatti;

@Repository
public interface ContattiRepository extends JpaRepository<Contatti, Integer> {

}
