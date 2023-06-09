package com.example.principal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.principal.model.Categoria;
import com.example.principal.repository.CategoriaRepository;

@Service
public class CategoriaService {
	@Autowired
	private CategoriaRepository categoriaRepository;

	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	public Optional<Categoria> findById(int id) {
		return categoriaRepository.findById(id);
	}

	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public List<Categoria> findByIds(List<Integer> ids) {
		return categoriaRepository.findAllById(ids);
	}

	public void delete(Categoria categoria) {
		categoriaRepository.delete(categoria);
	}

}