package com.example.principal.controller;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;
import jakarta.validation.Valid;

@Controller
public class CategoriaController {
	@Autowired
	private CategoriaService categoriaService;
// -------------------------------- INDEX -------------------------------------- //
		@GetMapping("/admin/categoria")
		public String index(Model model) {
			Optional<List<Categoria>> optionalCategorie = Optional.of(categoriaService.findAll());
			if (optionalCategorie.isPresent()) {
				model.addAttribute("categorie", optionalCategorie.get());
			} else {
				model.addAttribute("message", "Non ci sono categorie");
			}
			return "Categoria/index";
		}
// --------------------------------- CREATE --------------------------------------- //
		@GetMapping("/admin/categoria/create")
		public String create(Model model) {
		    List<Categoria> categorie = categoriaService.findAll();
		    model.addAttribute("categoria", new Categoria());
		    model.addAttribute("categorie", categorie);
		    return "Categoria/create";
		}
		@PostMapping("/admin/categoria/store")
		public String store(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult bindingResult, Model model) {
		    if (bindingResult.hasErrors()) {
		        for (ObjectError err : bindingResult.getAllErrors()) {
		            System.err.println("errore: " + err.getDefaultMessage());
		        }
		        model.addAttribute("errors", bindingResult);
		        return "Categoria/create";
		    }
		    categoriaService.save(categoria);   
		    return "redirect:/admin/categoria";
		}
// --------------------------------- DELETE --------------------------------------- //
		@GetMapping("/admin/categoria/delete/{id}")
		public String delete(@PathVariable int id) {
		    Optional<Categoria> categoriaOpt = categoriaService.findById(id);
		    Categoria categoria = categoriaOpt.get();
		    for (Foto foto : categoria.getFoto()) {
		        foto.getCategorie().remove(categoria);
		    }
		    categoriaService.delete(categoria);
		    return "redirect:/admin/categoria";
		}


}
