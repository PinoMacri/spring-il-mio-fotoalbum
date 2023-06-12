package com.example.principal.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.principal.auth.User;
import com.example.principal.model.Categoria;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>  {
	Set<Categoria> findByIdIn(Set<Integer> ids);
    List<Categoria> findByIdIn(List<Integer> ids);
    List<Categoria> findByUser(User user);

}

