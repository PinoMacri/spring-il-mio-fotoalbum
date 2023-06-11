package com.example.principal.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.principal.auth.Role;
import com.example.principal.auth.RoleService;
import com.example.principal.auth.User;
import com.example.principal.auth.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@GetMapping("/users/create")
	public String create(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "Utente/create";
	}

	@PostMapping("/users/store")
	public String store(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
	    String password = user.getPassword();
	    String encryptedPassword = new BCryptPasswordEncoder().encode(password);
	    user.setPassword(encryptedPassword);
	    Role adminRole = roleService.findByName("ADMIN");
	    user.setRoles(Collections.singleton(adminRole));
	    userService.save(user);

	    // Aggiungi il messaggio di successo alle RedirectAttributes
	    redirectAttributes.addFlashAttribute("successMessage", "Registrazione avvenuta con successo! Ora puoi effettuare il login.");

	    return "redirect:/";
	}

}
