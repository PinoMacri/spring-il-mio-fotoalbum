package com.example.principal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitatoreController {
	@GetMapping("/")
	public String getGuestHome() {
		return "home";
	}
}
