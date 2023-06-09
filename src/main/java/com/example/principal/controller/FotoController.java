package com.example.principal.controller;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;
import com.example.principal.service.FotoService;

import jakarta.validation.Valid;

@Controller
public class FotoController {
	@Autowired
	private FotoService fotoService;
	@Autowired
	private CategoriaService categoriaService;
// -------------------------------- INDEX -------------------------------------- //
	@GetMapping("/admin/foto")
	public String index(Model model) {
		Optional<List<Foto>> optionalFoto = Optional.of(fotoService.findAll());
		if (optionalFoto.isPresent()) {
			model.addAttribute("fotos", optionalFoto.get());
		} else {
			model.addAttribute("message", "Non ci sono foto");
		}
		return "Foto/index";
	}
// --------------------------------- SHOW --------------------------------------- //
	@GetMapping("/admin/foto/{id}")
	public String getFoto(Model model, @PathVariable("id") int id) {
		Foto foto = getFotoById(id);
		List<Categoria> categorie = categoriaService.findAll();
		model.addAttribute("foto", foto);
		model.addAttribute("categoria", categorie);
		return "Foto/show";
	}
	private Foto getFotoById(int id) {
		List<Foto> fotos = fotoService.findAll();
		Foto singolaFoto = null;
		for (Foto foto : fotos)
			if (foto.getId() == id)
				singolaFoto = foto;
		return singolaFoto;
	}
// --------------------------------- CREATE --------------------------------------- //
	@GetMapping("/admin/foto/create")
	public String create(Model model) {
	    List<Categoria> categorie = categoriaService.findAll();
	    model.addAttribute("foto", new Foto());
	    model.addAttribute("categorie", categorie);
	    model.addAttribute("categorieSelezionate", new HashSet<Integer>());
	    return "Foto/create";
	}
	@PostMapping("/admin/foto/store")
	public String store(Model model, @Valid @ModelAttribute Foto foto, BindingResult bindingResult,
	                    @RequestParam(value = "categorieSelezionate", required = false) Set<Integer> categorieSelezionate) {
	    System.out.println("Categorie selezionate: " + categorieSelezionate);
	    if (bindingResult.hasErrors()) {
	        for (ObjectError err : bindingResult.getAllErrors()) {
	            System.err.println("errore: " + err.getDefaultMessage());
	        }
	        if (foto.getVisibile() == null) {
	            foto.setVisibile(false);
	        }
	        model.addAttribute("foto", foto);
	        model.addAttribute("errors", bindingResult);
	        List<Categoria> categorie = categoriaService.findAll();
	        model.addAttribute("categorie", categorie);
	        model.addAttribute("categorieSelezionate", categorieSelezionate);
	        return "Foto/create";
	    }
	    if (categorieSelezionate != null) {
	        List<Categoria> categorie = categoriaService.findByIds(new ArrayList<>(categorieSelezionate));
	        foto.setCategorie(categorie);
	    } else {
	        foto.setCategorie(null); 
	    }
	    fotoService.save(foto);
	    return "redirect:/admin/foto";
	}
// --------------------------------- EDIT --------------------------------------- //
	@GetMapping("/admin/foto/edit/{id}")
	public String edit(Model model, @PathVariable int id) {
	    Optional<Foto> fotoOpt = fotoService.findById(id);
	    List<Categoria> categorie = categoriaService.findAll();
	    Foto foto = fotoOpt.get();
	    model.addAttribute("foto", foto);
	    model.addAttribute("categorie", categorie);
	    Set<Integer> categorieSelezionate = foto.getCategorie().stream().map(Categoria::getId).collect(Collectors.toSet());
	    model.addAttribute("categorieSelezionate", categorieSelezionate);
	    return "Foto/edit";
	}
	@PostMapping("/admin/foto/update/{id}")
	public String update(
	        Model model,
	        @PathVariable int id,
	        @Valid @ModelAttribute("foto") Foto foto,
	        BindingResult bindingResult,
	        @RequestParam(value = "categorieSelezionate", required = false) Set<Integer> categorieSelezionate) {

	    if (bindingResult.hasErrors()) {
	        for (ObjectError err : bindingResult.getAllErrors()) {
	            System.err.println("errore: " + err.getDefaultMessage());
	        }
	        model.addAttribute("errors", bindingResult);
	        List<Categoria> categorie = categoriaService.findAll();
	        model.addAttribute("categorie", categorie);
	        if (categorieSelezionate != null) {
	            model.addAttribute("categorieSelezionate", categorieSelezionate);
	        }
	        return "Foto/edit";
	    }
	    Foto existingFoto = fotoService.getFotoById(id);
	    if (existingFoto == null) {
	        return "redirect:/admin/foto";
	    }
	    existingFoto.setTitolo(foto.getTitolo());
	    existingFoto.setDescrizione(foto.getDescrizione());
	    existingFoto.setUrl(foto.getUrl());
	    existingFoto.setVisibile(foto.getVisibile());
	    if (categorieSelezionate != null) {
	        List<Categoria> categorie = categoriaService.findByIds(new ArrayList<>(categorieSelezionate));
	        existingFoto.setCategorie(categorie);
	    } else {
	        existingFoto.setCategorie(new ArrayList<>());
	    }
	    fotoService.save(existingFoto);
	    return "redirect:/admin/foto";
	}
// --------------------------------- DELETE --------------------------------------- //
	@GetMapping("/admin/foto/delete/{id}")
	public String delete(@PathVariable int id) {
		Optional<Foto> fotoOpt = fotoService.findById(id);
		Foto foto = fotoOpt.get();
		fotoService.delete(foto);
		return "redirect:/admin/foto";
	}
// --------------------------------- FILTRI --------------------------------------- //
	// Filtro Per Titolo:
	@PostMapping("/admin/foto/titolo")
	public String getFotoTitolo(@RequestParam(required = false) String titolo, Model model) {
		List<Foto> foto = fotoService.findByTitolo(titolo);
		model.addAttribute("fotos", foto);
		model.addAttribute("titolo", titolo);
		System.out.println(titolo);
		return "Foto/index";
	}


}
