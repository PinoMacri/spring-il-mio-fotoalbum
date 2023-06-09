package com.example.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.principal.auth.RoleService;
import com.example.principal.auth.UserService;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;
import com.example.principal.service.FotoService;

@SpringBootApplication
public class SpringFotografiaApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FotoService fotoService;
    @Autowired
    private CategoriaService categoriaService;

    public static void main(String[] args) {
        SpringApplication.run(SpringFotografiaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    /*    Role roleAdmin = new Role("ADMIN");
        roleService.save(roleAdmin);

        Role roleUser = new Role("USER");
        roleService.save(roleUser);

        final String password = new BCryptPasswordEncoder().encode("password");

        User userAdmin = new User("admin", password, roleAdmin);
        userService.save(userAdmin);

        User userUser = new User("user", password, roleUser);
        userService.save(userUser);
    } */
    /*	 Categoria c1 = new Categoria ("mozzarella","mozzacozza");
    	 Foto f1 = new Foto("Capalbio", "buona", "www.ciao.it", true, new Categoria[] {c1});
    	
         categoriaService.save(c1);
         fotoService.save(f1);
  
    	Categoria c1 = new Categoria ("mozzarella","mozzacozza");
    	Categoria c2 = new Categoria ("fgwefwefwe","mowefwefzzacozza");
    	Categoria c3 = new Categoria ("wefwefwefwef","wefwefwefwef");
    	Categoria c4 = new Categoria ("wefwefwefwef","wefwefwefwef");
    	
    	categoriaService.save(c1);
    	categoriaService.save(c2);
    	categoriaService.save(c3);
    	categoriaService.save(c4);
  	   */
    	

}}
