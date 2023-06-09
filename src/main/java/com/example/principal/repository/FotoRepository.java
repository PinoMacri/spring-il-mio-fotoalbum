package com.example.principal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.principal.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Integer> {
	 public List <Foto> findByTitoloContaining(String titolo);
}
