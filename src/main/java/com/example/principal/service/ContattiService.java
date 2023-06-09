package com.example.principal.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.principal.model.Contatti;
import com.example.principal.repository.ContattiRepository;

@Service
public class ContattiService {
	@Autowired
	private ContattiRepository contattiRepository;
	public Contatti save(Contatti contatti) {
		return contattiRepository.save(contatti);
	}
}
