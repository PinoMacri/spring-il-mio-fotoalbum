package com.example.principal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		if (foto.getVisibile() == null) {
            foto.setVisibile(false);
        }
        return fotoRepository.save(foto);
    }
	

	public List<Foto> findByTitolo(String titolo) {
		return fotoRepository.findByTitoloContaining(titolo);
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

}