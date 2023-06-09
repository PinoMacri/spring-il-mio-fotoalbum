package com.example.principal.model;

import java.util.Arrays;
import java.util.List;

import com.example.principal.model.Categoria;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Foto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Il Titolo non può essere vuoto")
	private String titolo;
	@Size(min = 2, max = 100, message = "La descrizione deve contenere minimo 2 e massimo 100 caratteri")
	private String descrizione;
	@NotBlank(message = "L'inserimento della foto è obbligatorio")
	private String url;
	private Boolean visibile;
	@ManyToMany
	private List <Categoria> categorie;
	public Foto() {
		
	}
	public Foto (String titolo, String descrizione, String url, Boolean visibile) {
		setTitolo(titolo);
		setDescrizione(descrizione);
		setUrl(url);
		setVisibile(visibile);
	}
	public Foto (String titolo, String descrizione, String url, Boolean visibile, Categoria... categorie) {
		setTitolo(titolo);
		setDescrizione(descrizione);
		setUrl(url);
		setVisibile(visibile);
		setsCategorie(categorie);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getVisibile() {
		return visibile != null ? visibile : false;
	}
	public void setVisibile(Boolean visibile) {
		this.visibile = visibile;
	}
	public List<Categoria> getCategorie() {
		return categorie;
	}
	public void setCategorie(List<Categoria> categorie) {
		this.categorie = categorie;
	}
	public void setsCategorie(Categoria[] categorie) {
		setCategorie(Arrays.asList(categorie));
	}
	@Override
	public String toString() {
		return "Foto [id=" + getId() + ", titolo=" + getTitolo() + ", descrizione=" + getDescrizione() + ", Url="
				+ getUrl() + ", visibile=" + getVisibile() + "]";
	}
	
	
}
