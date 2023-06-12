package com.example.principal.model.dto;

import java.util.List;

public class FotoDTO {
	 private int id;
	    private String titolo;
	    private String descrizione;
	    private String url;
	    private Boolean visibile;
	    private Boolean deleted;
	    private List<CategoriaDTO> categorie;
	    private UserDTO user;
	    public FotoDTO() {}
		public FotoDTO(int id, String titolo, String descrizione, String url, Boolean visibile, Boolean deleted,
				List<CategoriaDTO> categorie, UserDTO user) {
		
			this.id = id;
			this.titolo = titolo;
			this.descrizione = descrizione;
			this.url = url;
			this.visibile = visibile;
			this.deleted = deleted;
			this.categorie = categorie;
			this.user = user;
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
			return visibile;
		}
		public void setVisibile(Boolean visibile) {
			this.visibile = visibile;
		}
		public Boolean getDeleted() {
			return deleted;
		}
		public void setDeleted(Boolean deleted) {
			this.deleted = deleted;
		}
		public List<CategoriaDTO> getCategorie() {
			return categorie;
		}
		public void setCategorie(List<CategoriaDTO> categorie) {
			this.categorie = categorie;
		}
		public UserDTO getUser() {
			return user;
		}
		public void setUser(UserDTO user) {
			this.user = user;
		}
		@Override
		public String toString() {
			return "FotoDTO [id=" + id + ", titolo=" + titolo + ", descrizione=" + descrizione + ", url=" + url
					+ ", visibile=" + visibile + ", deleted=" + deleted + ", user=" + user + "]";
		}
	    
}
