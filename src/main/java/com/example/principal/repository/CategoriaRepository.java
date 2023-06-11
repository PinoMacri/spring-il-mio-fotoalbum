package com.example.principal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.principal.auth.User;
import com.example.principal.model.Categoria;


@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>  {
    List<Categoria> findByUser(User user);
}

