package com.example.principal.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.principal.model.Foto;
import com.example.principal.model.dto.CategoriaDTO;
import com.example.principal.model.dto.FotoDTO;
import com.example.principal.model.dto.RoleDTO;
import com.example.principal.model.dto.UserDTO;
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
	@GetMapping("/photos")
	public ResponseEntity<List<FotoDTO>> getPhotos() {
		List<Foto> fotoList = fotoService.findAll();

		List<FotoDTO> fotoDTOList = fotoList.stream().map(foto -> {
			FotoDTO fotoDTO = new FotoDTO();
			fotoDTO.setId(foto.getId());
			fotoDTO.setTitolo(foto.getTitolo());
			fotoDTO.setDescrizione(foto.getDescrizione());
			fotoDTO.setUrl(foto.getUrl());
			fotoDTO.setVisibile(foto.getVisibile());
			fotoDTO.setDeleted(foto.isDeleted());

			// Mappa le categorie nell'oggetto CategoriaDTO
			List<CategoriaDTO> categorieDTO = foto.getCategorie().stream().map(categoria -> {
				CategoriaDTO categoriaDTO = new CategoriaDTO();
				categoriaDTO.setId(categoria.getId());
				categoriaDTO.setNome(categoria.getNome());
				categoriaDTO.setDescrizione(categoria.getDescrizione());
				return categoriaDTO;
			}).collect(Collectors.toList());
			fotoDTO.setCategorie(categorieDTO);

			UserDTO userDTO = new UserDTO();
			userDTO.setId(foto.getUser().getId());
			userDTO.setUsername(foto.getUser().getUsername());

			// Mappa i ruoli dell'utente nell'oggetto RoleDTO
			List<RoleDTO> rolesDTO = foto.getUser().getRoles().stream().map(role -> {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(role.getId());
				roleDTO.setName(role.getName());
				return roleDTO;
			}).collect(Collectors.toList());
			userDTO.setRoles(rolesDTO);

			fotoDTO.setUser(userDTO);

			return fotoDTO;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(fotoDTOList);
	}

	// FILTRO FOTO PER NOME DI TUTTE LE FOTO DI TUTTI I FOTOGRAFI http://localhost:8080/api/foto/filter?titolo=
	@GetMapping("/filter")
	public ResponseEntity<List<FotoDTO>> getFoto(@RequestParam(required = false) String titolo) {
		List<Foto> fotos;
		if (titolo != null && !titolo.isEmpty()) {
			fotos = fotoService.findByTitolo(titolo);
		} else {
			fotos = fotoService.findAll();
		}

		List<FotoDTO> fotoDTOs = fotos.stream().map(foto -> {
			FotoDTO fotoDTO = new FotoDTO();
			fotoDTO.setId(foto.getId());
			fotoDTO.setTitolo(foto.getTitolo());
			fotoDTO.setDescrizione(foto.getDescrizione());
			fotoDTO.setUrl(foto.getUrl());
			fotoDTO.setVisibile(foto.getVisibile());
			fotoDTO.setDeleted(foto.isDeleted());

			// Mappa le categorie nell'oggetto CategoriaDTO
			List<CategoriaDTO> categorieDTO = foto.getCategorie().stream().map(categoria -> {
				CategoriaDTO categoriaDTO = new CategoriaDTO();
				categoriaDTO.setId(categoria.getId());
				categoriaDTO.setNome(categoria.getNome());
				categoriaDTO.setDescrizione(categoria.getDescrizione());
				return categoriaDTO;
			}).collect(Collectors.toList());
			fotoDTO.setCategorie(categorieDTO);

			UserDTO userDTO = new UserDTO();
			userDTO.setId(foto.getUser().getId());
			userDTO.setUsername(foto.getUser().getUsername());

			// Mappa i ruoli dell'utente nell'oggetto RoleDTO
			List<RoleDTO> rolesDTO = foto.getUser().getRoles().stream().map(role -> {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(role.getId());
				roleDTO.setName(role.getName());
				return roleDTO;
			}).collect(Collectors.toList());
			userDTO.setRoles(rolesDTO);

			fotoDTO.setUser(userDTO);

			return fotoDTO;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(fotoDTOs, HttpStatus.OK);
	}

// FILTRO PER FOTOGRAFO
	@GetMapping("/fotografi/{id}/foto")
	public ResponseEntity<List<FotoDTO>> getFotoByFotografo(@PathVariable int id) {
		List<Foto> fotos = fotoService.findByFotografo(id);

		List<FotoDTO> fotoDTOs = fotos.stream().map(foto -> {
			FotoDTO fotoDTO = new FotoDTO();
			fotoDTO.setId(foto.getId());
			fotoDTO.setTitolo(foto.getTitolo());
			fotoDTO.setDescrizione(foto.getDescrizione());
			fotoDTO.setUrl(foto.getUrl());
			fotoDTO.setVisibile(foto.getVisibile());
			fotoDTO.setDeleted(foto.isDeleted());

			// Mappa le categorie nell'oggetto CategoriaDTO
			List<CategoriaDTO> categorieDTO = foto.getCategorie().stream().map(categoria -> {
				CategoriaDTO categoriaDTO = new CategoriaDTO();
				categoriaDTO.setId(categoria.getId());
				categoriaDTO.setNome(categoria.getNome());
				categoriaDTO.setDescrizione(categoria.getDescrizione());
				return categoriaDTO;
			}).collect(Collectors.toList());
			fotoDTO.setCategorie(categorieDTO);

			UserDTO userDTO = new UserDTO();
			userDTO.setId(foto.getUser().getId());
			userDTO.setUsername(foto.getUser().getUsername());

			// Mappa i ruoli dell'utente nell'oggetto RoleDTO
			List<RoleDTO> rolesDTO = foto.getUser().getRoles().stream().map(role -> {
				RoleDTO roleDTO = new RoleDTO();
				roleDTO.setId(role.getId());
				roleDTO.setName(role.getName());
				return roleDTO;
			}).collect(Collectors.toList());
			userDTO.setRoles(rolesDTO);

			fotoDTO.setUser(userDTO);

			return fotoDTO;
		}).collect(Collectors.toList());

		return new ResponseEntity<>(fotoDTOs, HttpStatus.OK);
	}

	// FILTRO PER TITOLO DEL FOTOGRAFO
	@GetMapping("/filter/{idFotografo}")
	public ResponseEntity<List<FotoDTO>> getFotoByFotografoAndTitolo(@PathVariable int idFotografo, @RequestParam(required = false) String titolo) {
	    List<Foto> fotos;
	    if (titolo != null && !titolo.isEmpty()) {
	        fotos = fotoService.findByNomeAndFotografo(titolo, idFotografo);
	    } else {
	        fotos = fotoService.findByFotografo(idFotografo);
	    }

	    List<FotoDTO> fotosDTO = fotos.stream().map(foto -> {
	        FotoDTO fotoDTO = new FotoDTO();
	        fotoDTO.setId(foto.getId());
	        fotoDTO.setTitolo(foto.getTitolo());
	        fotoDTO.setDescrizione(foto.getDescrizione());
	        fotoDTO.setUrl(foto.getUrl());
	        fotoDTO.setVisibile(foto.getVisibile());
	        fotoDTO.setDeleted(foto.isDeleted());

	        // Mappa le categorie nell'oggetto CategoriaDTO
	        List<CategoriaDTO> categorieDTO = foto.getCategorie().stream().map(categoria -> {
	            CategoriaDTO categoriaDTO = new CategoriaDTO();
	            categoriaDTO.setId(categoria.getId());
	            categoriaDTO.setNome(categoria.getNome());
	            categoriaDTO.setDescrizione(categoria.getDescrizione());
	            return categoriaDTO;
	        }).collect(Collectors.toList());
	        fotoDTO.setCategorie(categorieDTO);

	        UserDTO userDTO = new UserDTO();
	        userDTO.setId(foto.getUser().getId());
	        userDTO.setUsername(foto.getUser().getUsername());

	        // Mappa i ruoli dell'utente nell'oggetto RoleDTO
	        List<RoleDTO> rolesDTO = foto.getUser().getRoles().stream().map(role -> {
	            RoleDTO roleDTO = new RoleDTO();
	            roleDTO.setId(role.getId());
	            roleDTO.setName(role.getName());
	            return roleDTO;
	        }).collect(Collectors.toList());
	        userDTO.setRoles(rolesDTO);

	        fotoDTO.setUser(userDTO);

	        return fotoDTO;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(fotosDTO);
	}

}
