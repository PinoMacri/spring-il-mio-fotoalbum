package com.example.principal.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.principal.auth.User;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.repository.CategoriaRepository;
import com.example.principal.repository.FotoRepository;

@Service
public class FotoService {
	@Autowired
	private FotoRepository fotoRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Foto> findAll() {
		return fotoRepository.findAll();
	}

	public Foto save(Foto foto) {
		if (!fotoRepository.existsByTitoloAndIdNotAndUser(foto.getTitolo(), foto.getId(), foto.getUser())) {
			// Il titolo non è duplicato o corrisponde al titolo originale
			if (foto.getVisibile() == null) {
				foto.setVisibile(false);
			}
			return fotoRepository.save(foto);
		} else {
			throw new IllegalStateException("Il Titolo deve essere unico");
		}
	}

	public List<Foto> findByTitolo(String titolo) {
		return fotoRepository.findByTitoloContaining(titolo);
	}

	public Optional<Foto> findFirstByTitolo(String titolo) {
		return fotoRepository.findByTitolo(titolo);
	}

	public void delete(Foto foto) {
		fotoRepository.delete(foto);
	}

	public Optional<Foto> findById(int id) {
		return fotoRepository.findById(id);
	}

	public Foto getFotoById(int id) {
		Optional<Foto> optionalFoto = fotoRepository.findById(id);
		if (optionalFoto.isPresent()) {
			return optionalFoto.get();
		} else {
			return null;
		}
	}

	public List<Foto> findByUser(User user) {
		return fotoRepository.findByUser(user);
	}

	public Optional<Foto> findByIdAndUser(int id, User user) {
		return fotoRepository.findByIdAndUser(id, user);
	}

	public List<Foto> findByTitoloContainingAndUser(String titolo, User user) {
		return fotoRepository.findByTitoloContainingAndUser(titolo, user);
	}
	public Page<Foto> findByTitoloContainingAndUser(String titolo, User user, PageRequest pageRequest) {
		return fotoRepository.findByTitoloContainingAndUser(titolo, user,  pageRequest);
	}

	public boolean existsByTitolo(String titolo) {
		return fotoRepository.existsByTitolo(titolo);
	}

	public boolean existsByTitoloAndIdNot(String titolo, int id) {
		Optional<Foto> existingFotoOpt = fotoRepository.findByTitoloAndIdNot(titolo, id);
		return existingFotoOpt.isPresent();
	}

	public Optional<Foto> findFirstByTitoloAndIdNot(String titolo, int id) {
		return fotoRepository.findFirstByTitoloAndIdNot(titolo, id);
	}

	public boolean existsByTitoloAndUser(String titolo, User user) {
		return fotoRepository.existsByTitoloAndUser(titolo, user);
	}

	public boolean existsByTitoloAndIdNotAndUser(String titolo, int id, User user) {
		Optional<Foto> existingFotoOpt = fotoRepository.findByTitoloAndIdNot(titolo, id);
		return existingFotoOpt.filter(foto -> foto.getUser().equals(user)).isPresent();
	}

	public void softDelete(Foto foto) {
		foto.setDeleted(true);
	}

	public List<Foto> findDeletedPhotos() {
		return fotoRepository.findByDeleted(true);
	}

	public List<Foto> findByCategorie(List<String> categorie) {
		return fotoRepository.findByCategorieIn(categorie);
	}

	public List<Foto> findByCategorieAndUser(List<String> categorie, User user) {
		return fotoRepository.findByCategorieInAndUser(categorie, user);
	}

	public List<Foto> findByTitoloContainingAndCategorieAndUser(String titolo, List<String> categorie, User user) {
		return fotoRepository.findByTitoloContainingAndCategorieInAndUser(titolo, categorie, user);
	}

	public List<Foto> findByTitoloAndCategorie(String titolo, List<String> categorie) {
		return fotoRepository.findByTitoloAndCategorieIn(titolo, categorie);
	}

	public List<Foto> findByUserAndCategorie(User user, List<Integer> categorieIds) {
		return fotoRepository.findByUserAndCategorieIdIn(user, categorieIds);
	}

	public List<Foto> findByTitoloContainingAndUserAndCategoria(String titolo, User user,
			Set<Integer> categorieSelezionate) {
		List<Foto> foto;

		if (categorieSelezionate != null && !categorieSelezionate.isEmpty()) {
			// Ottieni l'insieme di categorie selezionate dall'ID
			List<Categoria> categorieList = categoriaRepository.findAllById(categorieSelezionate);
			Set<Categoria> categorie = new HashSet<>(categorieList);

			// Esegui la query per ottenere le foto con il titolo contenente, appartenenti
			// all'utente specificato e alle categorie selezionate
			foto = fotoRepository.findByTitoloContainingAndUserAndCategorieIn(titolo, user, categorie);
		} else {
			// Esegui la query per ottenere le foto con il titolo contenente e appartenenti
			// all'utente specificato
			foto = fotoRepository.findByTitoloContainingAndUser(titolo, user);
		}

		return foto;
	}

	public FotoService(FotoRepository fotoRepository) {
		this.fotoRepository = fotoRepository;
	}

	public List<Foto> findByTitoloContainingAndCategorie(String titolo, Set<Integer> categorieSelezionate) {
		Set<Categoria> categorie = categoriaRepository.findByIdIn(categorieSelezionate);
		return fotoRepository.findByTitoloContainingAndCategorieIn(titolo, categorie);
	}

	public List<Foto> findByTitoloContainingAndUserAndCategorie(String titolo, User user,
			Set<Integer> categorieSelezionate) {
		Set<Categoria> categorie = categoriaRepository.findByIdIn(categorieSelezionate);
		return fotoRepository.findByTitoloContainingAndUserAndCategorieIn(titolo, user, categorie);
	}

	public List<Foto> findAllByCategorie(Set<Integer> categorieSelezionate) {
		Set<Categoria> categorie = categoriaRepository.findByIdIn(categorieSelezionate);
		return fotoRepository.findAllByCategorieIn(categorie);
	}

	public List<Foto> findByUserAndCategorie(User user, Set<Integer> categorieSelezionate) {
		Set<Categoria> categorie = categoriaRepository.findByIdIn(categorieSelezionate);
		return fotoRepository.findByUserAndCategorieIn(user, categorie);
	}

	public List<Foto> findByTitoloAndCategorie(String titolo, Set<Integer> categorieSelezionate) {
		if (categorieSelezionate == null || categorieSelezionate.isEmpty()) {
			return fotoRepository.findByTitoloContaining(titolo);
		} else {
			Set<Categoria> categorie = categoriaRepository.findByIdIn(categorieSelezionate);
			return fotoRepository.findByTitoloContainingAndCategorieIn(titolo, categorie);
		}
	}

	public List<Foto> findByTitoloAndUser(String titolo, User user) {
		return fotoRepository.findByTitoloContainingAndUser(titolo, user);
	}

	public List<Foto> findByTitoloContainingAndCategorieAndUser(String titolo, Set<Integer> categorieSelezionate,
			User user) {
		List<Foto> foto;

		if (categorieSelezionate != null && !categorieSelezionate.isEmpty()) {
			// Ottieni l'insieme di categorie selezionate dall'ID
			List<Categoria> categorieList = categoriaRepository.findAllById(categorieSelezionate);
			Set<Categoria> categorie = new HashSet<>(categorieList);

			// Esegui la query per ottenere le foto con il titolo contenente, appartenenti
			// all'utente specificato e alle categorie selezionate
			foto = fotoRepository.findByTitoloContainingAndCategorieInAndUser(titolo, categorie, user);
		} else {
			// Esegui la query per ottenere le foto con il titolo contenente e appartenenti
			// all'utente specificato
			foto = fotoRepository.findByTitoloContainingAndUser(titolo, user);
		}

		return foto;
	}

	public Foto getPhotoById(int id) {
		Optional<Foto> optionalFoto = fotoRepository.findById(id);
		if (optionalFoto.isPresent()) {
			return optionalFoto.get();
		} else {
			// Gestione dell'errore nel caso in cui la foto non sia presente
			throw new RuntimeException("Foto non trovata per l'ID: " + id);
		}
	}

	public List<Foto> findByFotografo(int id) {
		return fotoRepository.findByUser_Id(id);
	}

	public List<Foto> findByNomeAndFotografo(String titolo, int id) {
		return fotoRepository.findByNomeStartingWithAndUser_Id(titolo, id);
	}

	public Page<Foto> findAll(PageRequest pageRequest) {
		List<Foto> allFotos = fotoRepository.findAll(); // sostituisci con il tuo metodo di accesso al database per
														// ottenere tutte le foto
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), allFotos.size());
		return new PageImpl<>(allFotos.subList(start, end), pageRequest, allFotos.size());
	}

	public Page<Foto> findByUser(User user, PageRequest pageRequest) {
		List<Foto> userFotos = fotoRepository.findByUser(user); // sostituisci con il tuo metodo di accesso al database
																// per ottenere le foto dell'utente
		int start = (int) pageRequest.getOffset();
		int end = Math.min((start + pageRequest.getPageSize()), userFotos.size());
		return new PageImpl<>(userFotos.subList(start, end), pageRequest, userFotos.size());
	}

	public Page<Foto> findByTitoloAndUser(String titolo, User user, PageRequest of) {
		return fotoRepository.findByTitoloContainingAndUser(titolo, user, of);
	}

	public Page<Foto> findByTitoloContaining(String titolo, PageRequest pageable) {
		return fotoRepository.findByTitoloContainingIgnoreCase(titolo, pageable);
	}

	public Page<Foto> findAllByCategorie(Set<Integer> categorieSelezionate, PageRequest of) {
	    List<Foto> fotoList = new ArrayList<>();

	    for (Integer categoriaId : categorieSelezionate) {
	        Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoriaId);
	        if (categoriaOptional.isPresent()) {
	            Categoria categoria = categoriaOptional.get();
	            fotoList.addAll(categoria.getFoto());
	        }
	    }

	    fotoList.sort(Comparator.comparing(Foto::getId));

	    int startIndex = 0;
	    int pageSize = 10;
	    if (of != null) {
	        startIndex = of.getPageNumber() * of.getPageSize();
	        pageSize = of.getPageSize();
	    }

	    int endIndex = Math.min(startIndex + pageSize, fotoList.size());

	    if (startIndex >= fotoList.size()) {
	        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, pageSize), fotoList.size());
	    }

	    List<Foto> pageFotoList = fotoList.subList(startIndex, endIndex);
	    return new PageImpl<>(pageFotoList, PageRequest.of(0, pageSize), fotoList.size());
	}

	public Page<Foto> findByUserAndCategorie(User user, Set<Integer> categorieSelezionate, PageRequest of) {
	    List<Foto> fotoList = new ArrayList<>();

	    for (Integer categoriaId : categorieSelezionate) {
	        Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoriaId);
	        if (categoriaOptional.isPresent()) {
	            Categoria categoria = categoriaOptional.get();
	            fotoList.addAll(categoria.getFoto().stream()
	                    .filter(foto -> foto.getUser().equals(user))
	                    .collect(Collectors.toList()));
	        }
	    }

	    fotoList.sort(Comparator.comparing(Foto::getId));

	    int startIndex = 0;
	    int pageSize = 10;
	    if (of != null) {
	        startIndex = of.getPageNumber() * of.getPageSize();
	        pageSize = of.getPageSize();
	    }

	    int endIndex = Math.min(startIndex + pageSize, fotoList.size());

	    if (startIndex >= fotoList.size()) {
	        return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, pageSize), fotoList.size());
	    }

	    List<Foto> pageFotoList = fotoList.subList(startIndex, endIndex);
	    return new PageImpl<>(pageFotoList, PageRequest.of(0, pageSize), fotoList.size());
	}

	




}