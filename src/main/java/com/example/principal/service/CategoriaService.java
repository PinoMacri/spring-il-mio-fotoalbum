package com.example.principal.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.principal.auth.User;
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

	
	public List<Categoria> findByUser(User user) {
	    return categoriaRepository.findByUser(user);
	}

	public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Set<Categoria> getCategorieByIds(Set<Integer> ids) {
        return new HashSet<>(categoriaRepository.findAllById(ids));
    }
    
    public Set<Categoria> findByIdIn(Set<Integer> ids) {
        return categoriaRepository.findByIdIn(ids);
    }
    
    
    
  
}