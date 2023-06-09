package com.example.principal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Contatti {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "La mail non può essere vuota")
	private String email;
	@Size(min = 2, max = 1000, message = "Il messaggio deve contenere minimo 2 e massimo 1000 caratteri")
	private String messaggio;

	public Contatti() {

	}

	public Contatti(String email, String messaggio) {
		setEmail(email);
		setMessaggio(messaggio);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	@Override
	public String toString() {
		return "Email :" + getEmail() + " Messaggio: " + getMessaggio();
	}
}