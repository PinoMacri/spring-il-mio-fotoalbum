package com.example.principal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.principal.auth.User;
import com.example.principal.model.Foto;
import com.example.principal.repository.FotoRepository;

@Service
public class FotoService {
	@Autowired
	private FotoRepository fotoRepository;

	public List<Foto> findAll() {
		return fotoRepository.findAll();
	}

	public Foto save(Foto foto) {
	    if (!fotoRepository.existsByTitoloAndIdNot(foto.getTitolo(), foto.getId())) {
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



}