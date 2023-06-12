package com.example.principal.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.principal.auth.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Foto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank(message = "Il Titolo non può essere vuoto")
	@Column(unique = true)
	private String titolo;
	@Size(min = 2, max = 100, message = "La descrizione deve contenere minimo 2 e massimo 100 caratteri")
	private String descrizione;
	@NotBlank(message = "L'inserimento della foto è obbligatorio")
	private String url;
	private Boolean visibile = false;
	
	private boolean deleted = false;

	
	@ManyToOne
	private User user;

	@ManyToMany(fetch = FetchType.EAGER)
	private List <Categoria> categorie;
	public Foto() {
		
	}
	public Foto (String titolo, String descrizione, String url, Boolean visibile) {
		setTitolo(titolo);
		setDescrizione(descrizione);
		setUrl(url);
		setVisibile(visibile);
	}
	public Foto (String titolo, String descrizione, String url, Boolean visibile, User user, Categoria... categorie) {
		setTitolo(titolo);
		setDescrizione(descrizione);
		setUrl(url);
		setVisibile(visibile);
		setUser(user);
		setsCategorie(categorie);
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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
	@Transient
	public List<Map<String, String>> getCategorieInfo() {
	    List<Map<String, String>> categorieInfo = new ArrayList<>();
	    for (Categoria categoria : categorie) {
	        Map<String, String> info = new HashMap<>();
	        info.put("nome", categoria.getNome());
	        info.put("descrizione", categoria.getDescrizione());
	        categorieInfo.add(info);
	    }
	    return categorieInfo;
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
	@Transient
	public Map<String, Object> getUserInfo() {
	    Map<String, Object> userInfo = new HashMap<>();
	    userInfo.put("id", user.getId());
	    return userInfo;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Foto [id=" + getId() + ", titolo=" + getTitolo() + ", descrizione=" + getDescrizione() + ", Url="
				+ getUrl() + ", visibile=" + getVisibile() + "]";
	}
	
	
}
