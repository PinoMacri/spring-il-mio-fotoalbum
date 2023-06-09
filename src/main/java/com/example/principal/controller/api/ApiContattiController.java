package com.example.principal.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.principal.model.Contatti;
import com.example.principal.service.ContattiService;

@RestController
@CrossOrigin
@RequestMapping("/api/contatti")
public class ApiContattiController {
	@Autowired
	private ContattiService contattiService;
	// CREATE
	@PostMapping("/create")
	public ResponseEntity<Contatti> create(@RequestBody Contatti contatti) {	
	Contatti savedContatto = contattiService.save(contatti);
	System.out.println(contatti);
	return new ResponseEntity<>(savedContatto, HttpStatus.CREATED);
	}
}
