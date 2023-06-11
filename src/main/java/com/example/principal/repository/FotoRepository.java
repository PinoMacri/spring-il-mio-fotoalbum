package com.example.principal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.principal.auth.User;
import com.example.principal.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Integer> {
	 public List <Foto> findByTitoloContaining(String titolo);
	 
	 public Optional <Foto> findByTitolo(String titolo);

	public List<Foto> findByUser(User user);

	public Optional<Foto> findByIdAndUser(int id, User user);

	public List<Foto> findByTitoloContainingAndUser(String titolo, User user);
	
    boolean existsByTitolo(String titolo);

	public boolean existsByTitoloAndIdNot(String titolo, int id);
	
	@Query("SELECT f FROM Foto f WHERE f.titolo = :titolo AND f.id <> :id")
	Optional<Foto> findByTitoloAndIdNot(@Param("titolo") String titolo, @Param("id") int id);

	Optional<Foto> findFirstByTitoloAndIdNot(String titolo, int id);




}
