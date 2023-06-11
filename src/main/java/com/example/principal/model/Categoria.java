package com.example.principal.model;

import java.util.List;

import com.example.principal.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Categoria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotNull
	@Size(min = 2, max = 255)
	private String nome;
	@Size(min = 2, max = 255)
	private String descrizione;

	@JsonIgnore
	@ManyToMany(mappedBy = "categorie")
	private List<Foto> foto;
	@ManyToOne
    private User user;

	public Categoria() {
	}

	public Categoria(String nome) {
		setNome(nome);
	}

	public Categoria(String nome, String descrizione) {
		this(nome);
		setDescrizione(descrizione);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public List<Foto> getFoto() {
		return foto;
	}

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
	public void setFoto(List<Foto> foto) {
		this.foto = foto;
	}
	
}
