package com.example.principal.model.dto;

public class CategoriaDTO {
	private int id;
    private String nome;
    private String descrizione;
    public CategoriaDTO() {}
	public CategoriaDTO(int id, String nome, String descrizione) {
		this.id = id;
		this.nome = nome;
		this.descrizione = descrizione;
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
	@Override
	public String toString() {
		return "CategoriaDTO [id=" + id + ", nome=" + nome + ", descrizione=" + descrizione + "]";
	}
	
}
