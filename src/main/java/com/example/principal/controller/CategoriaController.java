package com.example.principal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.principal.auth.User;
import com.example.principal.auth.UserService;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;

import jakarta.validation.Valid;

@Controller
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UserService userService;

    // -------------------------------- INDEX -------------------------------------- //
    @GetMapping("/admin/categoria")
    public String index(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean isSuperAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));
        Optional<List<Categoria>> optionalCategorie;
        
        if (isSuperAdmin) {
            optionalCategorie = Optional.of(categoriaService.findAll());
        } else {
            String username = authentication.getName();
            Optional<User> userOptional = userService.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                optionalCategorie = Optional.of(categoriaService.findByUser(user));
            } else {
                optionalCategorie = Optional.empty();
            }
        }

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
    public String store(@Valid @ModelAttribute("categoria") Categoria categoria, BindingResult bindingResult, Model model, Authentication authentication) {
    	if (bindingResult.hasErrors()) {
            for (ObjectError err : bindingResult.getAllErrors()) {
                System.err.println("errore: " + err.getDefaultMessage());
            }
            model.addAttribute("errors", bindingResult);
            return "Categoria/create";
        }
        
        // Imposta l'utente corrente come proprietario della categoria
        String username = authentication.getName();
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            categoria.setUser(user);
        } else {
            // Gestione caso utente non trovato
            model.addAttribute("message", "Utente non trovato");
            return "Categoria/create";
        }

        // Salva la categoria
        categoriaService.save(categoria);
        return "redirect:/admin/categoria";
    }

    // --------------------------------- DELETE --------------------------------------- //
    @GetMapping("/admin/categoria/delete/{id}")
    public String delete(@PathVariable int id, Authentication authentication) {
        Optional<Categoria> categoriaOpt = categoriaService.findById(id);
        if (categoriaOpt.isPresent()) {
            Categoria categoria = categoriaOpt.get();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            boolean isSuperAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));
            if (isSuperAdmin) {
                for (Foto foto : categoria.getFoto()) {
                    foto.getCategorie().remove(categoria);
                }
                categoriaService.delete(categoria);
            } else {
                String username = authentication.getName();
                Optional<User> userOptional = userService.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (categoria.getUser().equals(user)) {
                        for (Foto foto : categoria.getFoto()) {
                            if (foto.getUser().equals(user)) {
                                foto.getCategorie().remove(categoria);
                            }
                        }
                        categoriaService.delete(categoria);
                    }
                }
            }
        }
        return "redirect:/admin/categoria";
    }
}
