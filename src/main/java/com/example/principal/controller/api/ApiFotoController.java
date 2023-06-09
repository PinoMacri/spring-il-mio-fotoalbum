package com.example.principal.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;
import com.example.principal.service.FotoService;

@RestController
@CrossOrigin
@RequestMapping("/api/foto")
public class ApiFotoController {
	@Autowired
	private FotoService fotoService;
	@Autowired
	private CategoriaService categoriaService;

	// INDEX
	@GetMapping("/lista")
	public ResponseEntity<List<Foto>> getFoto() {
		List<Foto> fotos = fotoService.findAll();
		return new ResponseEntity<>(fotos, HttpStatus.OK);
	}

	// FILTRO FOTO PER NOME http://localhost:8080/api/foto/filter?titolo=
	@GetMapping("/filter")
	public ResponseEntity<List<Foto>> getFoto(@RequestParam(required = false) String titolo) {
		List<Foto> fotos;
		if (titolo != null && !titolo.isEmpty()) {
			fotos = fotoService.findByTitolo(titolo);
		} else {
			fotos = fotoService.findAll();
		}
		return new ResponseEntity<>(fotos, HttpStatus.OK);
	}
}
