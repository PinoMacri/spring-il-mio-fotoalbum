package com.example.principal.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.principal.auth.User;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, Integer> {
	public List<Foto> findByTitoloContaining(String titolo);

	public Optional<Foto> findByTitolo(String titolo);

	public List<Foto> findByUser(User user);

	public Optional<Foto> findByIdAndUser(int id, User user);

	public List<Foto> findByTitoloContainingAndUser(String titolo, User user);

	Page<Foto> findByTitoloContainingAndUser(String titolo, User user, PageRequest pageable);

	Page<Foto> findByTitoloContainingIgnoreCase(String titolo, PageRequest pageable);



	boolean existsByTitolo(String titolo);

	public boolean existsByTitoloAndIdNot(String titolo, int id);

	@Query("SELECT f FROM Foto f WHERE f.titolo = :titolo AND f.id <> :id")
	Optional<Foto> findByTitoloAndIdNot(@Param("titolo") String titolo, @Param("id") int id);

	Optional<Foto> findFirstByTitoloAndIdNot(String titolo, int id);

	public boolean existsByTitoloAndUser(String titolo, User user);

	boolean existsByTitoloAndIdNotAndUser(String titolo, int id, User user);

	public List<Foto> findByDeleted(boolean b);

	List<Foto> findByCategorieIn(List<String> categorie);

	List<Foto> findByCategorieInAndUser(List<String> categorie, User user);

	List<Foto> findByTitoloContainingAndCategorieInAndUser(String titolo, List<String> categorie, User user);

	List<Foto> findByTitoloAndCategorieIn(String titolo, List<String> categorie);

	public List<Foto> findByUserAndCategorieIn(User user, List<String> categorie);

	public List<Foto> findByUserAndCategorieIdIn(User user, List<Integer> categorieIds);

	public Optional<Foto> findFirstByTitolo(String titolo);

	List<Foto> findByTitoloContainingAndUserAndCategorieIn(String titolo, User user, Set<Categoria> categorie);

	List<Foto> findByTitoloContainingAndCategorieIn(String titolo, Set<Categoria> categorie);

	List<Foto> findByUserAndCategorieIn(User user, Set<Categoria> categorie);

	List<Foto> findAllByCategorieIn(Set<Categoria> categorie);

	public List<Foto> findByTitoloContainingAndCategorieInAndUser(String titolo, Set<Categoria> categorie, User user);

	List<Foto> findByUser_Id(int idFotografo);

	@Query("SELECT f FROM Foto f WHERE f.titolo LIKE CONCAT(:nome, '%') AND f.user.id = :idFotografo")
	List<Foto> findByNomeStartingWithAndUser_Id(String nome, int idFotografo);

}
