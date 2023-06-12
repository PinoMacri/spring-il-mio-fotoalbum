package com.example.principal.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.principal.auth.User;
import com.example.principal.auth.UserService;
import com.example.principal.model.Categoria;
import com.example.principal.model.Foto;
import com.example.principal.service.CategoriaService;
import com.example.principal.service.FotoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class FotoController {
	@Autowired
	private FotoService fotoService;
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private UserService userService;

// -------------------------------- INDEX -------------------------------------- //
	@GetMapping("/admin/foto")
	public String index(Model model, Authentication authentication, @RequestParam(defaultValue = "0") int page) {
	    String username = authentication.getName();
	    Optional<User> userOptionals = userService.findByUsername(username);
	    User users = userOptionals.get();

	    if (authentication != null) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SUPERADMIN"))) {
	            Page<Foto> fotoPage = fotoService.findAll(PageRequest.of(page, 15));
	            model.addAttribute("fotos", fotoPage.getContent());
	            model.addAttribute("currentPage", page);
	            model.addAttribute("totalPages", fotoPage.getTotalPages());
	        } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
	            Optional<User> userOptional = userService.findByUsername(username);
	            if (userOptional.isPresent()) {
	                User user = userOptional.get();
	                Page<Foto> fotoPage = fotoService.findByUser(user, PageRequest.of(page, 15));
	                model.addAttribute("fotos", fotoPage.getContent());
	                model.addAttribute("currentPage", page);
	                model.addAttribute("totalPages", fotoPage.getTotalPages());
	            } else {
	                model.addAttribute("message", "Utente non trovato");
	            }
	        }
	    }
	    
	    boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
	    model.addAttribute("isAdmin", isAdmin);
	    boolean isSuperAdmin = authentication.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));
	    model.addAttribute("isSuperAdmin", isSuperAdmin);
	    
	    List<Categoria> categorie = new ArrayList<>();
	    if (isSuperAdmin) {
	        categorie = categoriaService.findAll();
	    } else {
	        categorie = categoriaService.findByUser(users);
	    }
	    model.addAttribute("categorie", categorie);
	    model.addAttribute("username", username);
	    
	    return "Foto/index";
	}

// --------------------------------- SHOW --------------------------------------- //
	@GetMapping("/admin/foto/{id}")
	public String getFoto(Model model, @PathVariable("id") int id, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Optional<Foto> fotoOptional = fotoService.findById(id);
			if (fotoOptional.isPresent()) {
				Foto foto = fotoOptional.get();
				boolean isSuperAdmin = authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));

				if (isSuperAdmin || foto.getUser().equals(user)) {
					List<Categoria> categorie = categoriaService.findAll();
					model.addAttribute("foto", foto);
					model.addAttribute("categoria", categorie);
					return "Foto/show";
				}
			}
		}
		model.addAttribute("message", "Foto non trovata");
		return "Foto/errors";
	}

// --------------------------------- CREATE --------------------------------------- //
	@GetMapping("/admin/foto/create")
	public String create(Model model, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			List<Categoria> categorie = user.getCategorie();
			model.addAttribute("foto", new Foto());
			model.addAttribute("categorie", categorie);
			model.addAttribute("categorieSelezionate", new HashSet<Integer>());
			return "Foto/create";
		} else {
			model.addAttribute("message", "Utente non trovato");
			return "Foto/error";
		}
	}

	@PostMapping("/admin/foto/store")
	public String store(Model model, @Valid @ModelAttribute Foto foto, BindingResult bindingResult,
			@RequestParam(value = "categorieSelezionate", required = false) Set<Integer> categorieSelezionate,
			Authentication authentication) {

		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();

			System.out.println("Categorie selezionate: " + categorieSelezionate);
			// Aggiungi questa riga per ottenere solo le categorie dell'admin corrente
			List<Categoria> categorieAdmin = user.getCategorie();

			try {
				String titoloTrimmed = foto.getTitolo().trim();
				foto.setTitolo(titoloTrimmed);

				if (fotoService.existsByTitolo(foto.getTitolo())) {
					throw new IllegalStateException("Il Titolo deve essere unico");
				}

				if (bindingResult.hasErrors()) {
					for (ObjectError err : bindingResult.getAllErrors()) {
						System.err.println("errore: " + err.getDefaultMessage());
					}

					model.addAttribute("foto", foto);
					model.addAttribute("errors", bindingResult);

					// Aggiorna questa riga per utilizzare solo le categorie dell'admin corrente
					model.addAttribute("categorie", categorieAdmin);

					model.addAttribute("categorieSelezionate", categorieSelezionate);
					return "Foto/create";
				}

				if (categorieSelezionate != null) {
					// Filtra le categorie selezionate per assicurarti che siano solo quelle
					// dell'admin corrente
					List<Categoria> categorie = categorieAdmin.stream()
							.filter(cat -> categorieSelezionate.contains(cat.getId())).collect(Collectors.toList());

					foto.setCategorie(categorie);
				} else {
					foto.setCategorie(null);
				}
				foto.setUser(user);
				fotoService.save(foto);
				 String successMessage = "Creazione avvenuta con successo!";
				
				 return "redirect:/admin/foto?successMessage=" + successMessage;

			} catch (IllegalStateException e) {
				model.addAttribute("errorMessage", e.getMessage());

				model.addAttribute("foto", foto);
				model.addAttribute("errors", bindingResult);

				// Aggiorna questa riga per utilizzare solo le categorie dell'admin corrente
				model.addAttribute("categorie", categorieAdmin);

				model.addAttribute("categorieSelezionate", categorieSelezionate);
				return "Foto/create";
			}
		} else {
			model.addAttribute("message", "Utente non trovato");
			return "Foto/error";
		}
	}

	// --------------------------------- EDIT
	// --------------------------------------- //
	@GetMapping("/admin/foto/edit/{id}")
	public String edit(Model model, @PathVariable int id, Authentication authentication) {
		String username = authentication.getName();
		Optional<User> userOptional = userService.findByUsername(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Optional<Foto> fotoOpt = fotoService.findById(id);

			if (fotoOpt.isPresent()) {
				Foto foto = fotoOpt.get();
				boolean isAdmin = authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("ADMIN"));
				boolean isSuperAdmin = authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));

				if ((isAdmin && foto.getUser().equals(user)) || isSuperAdmin) {
					List<Categoria> categorie;
					if (isSuperAdmin) {
						categorie = categoriaService.findByUser(foto.getUser()); // Ottieni le categorie dell'admin che
																					// ha creato la foto
					} else {
						categorie = user.getCategorie();
					}
					model.addAttribute("foto", foto);
					model.addAttribute("categorie", categorie);
					Set<Integer> categorieSelezionate = foto.getCategorie().stream().map(Categoria::getId)
							.collect(Collectors.toSet());
					model.addAttribute("categorieSelezionate", categorieSelezionate);
					return "Foto/edit";
				}
			}
		}

		return "redirect:/admin/foto";
	}

	@PostMapping("/admin/foto/update/{id}")
	public String update(Model model, @PathVariable int id, @Valid @ModelAttribute("foto") Foto foto,
			BindingResult bindingResult,
			@RequestParam(value = "categorieSelezionate", required = false) Set<Integer> categorieSelezionate,
			Authentication authentication) {

		String username = authentication.getName();
		Optional<User> user = userService.findByUsername(username);
		if (user.isPresent()) {
			Optional<Foto> existingFotoOpt = fotoService.findById(id);
			if (existingFotoOpt.isPresent()) {
				Foto existingFoto = existingFotoOpt.get();

				boolean isAdmin = authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("ADMIN"));
				boolean isSuperAdmin = authentication.getAuthorities().stream()
						.anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));

				if ((isAdmin && existingFoto.getUser().equals(user.get())) || isSuperAdmin) {
					try {
						String titoloTrimmed = foto.getTitolo().trim();
						foto.setTitolo(titoloTrimmed);

						// Controllo del titolo unico solo per il SuperAdmin
						if (isSuperAdmin
								&& fotoService.existsByTitoloAndUser(foto.getTitolo(), existingFoto.getUser())) {
							throw new IllegalStateException("Il Titolo deve essere unico");
						}

						if (bindingResult.hasErrors()) {
							for (ObjectError err : bindingResult.getAllErrors()) {
								System.err.println("errore: " + err.getDefaultMessage());
							}
							List<Categoria> categorie;
							if (isAdmin) {
								categorie = categoriaService.findByUser(user.get());
							} else {
								categorie = categoriaService.findByUser(existingFoto.getUser()); // Ottieni le categorie
																									// dell'admin che ha
																									// creato la foto
							}
							model.addAttribute("errors", bindingResult);
							model.addAttribute("categorie", categorie);
							model.addAttribute("categorieSelezionate", categorieSelezionate); // Aggiungi le categorie
																								// selezionate al model
							return "Foto/edit";
						}
						existingFoto.setTitolo(foto.getTitolo()); // Aggiorna il titolo della foto
						existingFoto.setDescrizione(foto.getDescrizione());
						existingFoto.setUrl(foto.getUrl());
						existingFoto.setVisibile(foto.getVisibile());

						// Aggiorna le categorie solo se l'utente è un SuperAdmin e non ci sono errori
						// di validazione
						if (isSuperAdmin && !bindingResult.hasErrors()) {
							if (categorieSelezionate != null) {
								List<Categoria> categorie = categoriaService
										.findByIds(new ArrayList<>(categorieSelezionate));
								existingFoto.setCategorie(categorie);
							} else {
								existingFoto.setCategorie(new ArrayList<>());
							}
						}

						fotoService.save(existingFoto);
						 String successMessages = "Modifica avvenuta con successo!";
						 return "redirect:/admin/foto?successMessage=" + successMessages;
					} catch (IllegalStateException e) {
						model.addAttribute("errorMessage", e.getMessage());
						List<Categoria> categorie;
						if (isAdmin) {
							categorie = categoriaService.findByUser(user.get());
						} else {
							categorie = categoriaService.findByUser(existingFoto.getUser()); // Ottieni le categorie
																								// dell'admin che ha
																								// creato la foto
						}
						model.addAttribute("errors", bindingResult);
						model.addAttribute("categorie", categorie);
						model.addAttribute("categorieSelezionate", categorieSelezionate); // Aggiungi le categorie
																							// selezionate al model
						return "Foto/edit";
					}
				}
			}
		}

		return "redirect:/admin/foto";
	}

	// --------------------------------- SOFT DELETE
	// --------------------------------------- //
	@PostMapping("/admin/foto/softdelete/{id}")
	@Transactional
	public String softDeleteFoto(@PathVariable("id") int id) {
		Optional<Foto> fotoOptional = fotoService.findById(id);
		if (fotoOptional.isPresent()) {
			Foto foto = fotoOptional.get();
			foto.setDeleted(true);
			// Non è necessario salvare una nuova istanza di Foto, basta aggiornare
			// l'istanza esistente
			return "redirect:/admin/foto";
		} else {
			// Gestione dell'errore
			// La foto non è stata trovata
			// Puoi mostrare un messaggio di errore o fare altre azioni di gestione
			return "redirect:/admin/foto";
		}
	}

	@GetMapping("/admin/foto/cestino")
	public String getFotoCestino(Model model, Authentication authentication) {
		String username = authentication.getName();
		model.addAttribute("username", username);
		List<Foto> fotoCestino = fotoService.findDeletedPhotos();
		model.addAttribute("fotoCestino", fotoCestino);
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
		model.addAttribute("isAdmin", isAdmin);
		boolean isSuperAdmin = authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));
		model.addAttribute("isSuperAdmin", isSuperAdmin);
		return "Foto/cestino";
	}

	@PostMapping("/admin/foto/ripristina/{id}")
	public String ripristinaFoto(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
	    Optional<Foto> fotoOptional = fotoService.findById(id);
	    if (fotoOptional.isPresent()) {
	        Foto foto = fotoOptional.get();
	        foto.setDeleted(false);
	        fotoService.save(foto);
	        String successMessages = "Ripristino avvenuto con successo!";
			 return "redirect:/admin/foto?successMessage=" + successMessages;

	        // Aggiungi il messaggio di successo come attributo flash
	       
	    }
	    return "redirect:/admin/foto";
	}

	@PostMapping("/admin/foto/delete/{id}")
	public String delete(@PathVariable int id, Authentication authentication, RedirectAttributes redirectAttributes) {
	    String username = authentication.getName();
	    Optional<User> userOptional = userService.findByUsername(username);
	    if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        Optional<Foto> fotoOpt = fotoService.findById(id);
	        if (fotoOpt.isPresent()) {
	            Foto foto = fotoOpt.get();

	            boolean isAdmin = authentication.getAuthorities().stream()
	                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
	            boolean isSuperAdmin = authentication.getAuthorities().stream()
	                    .anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));

	            if ((isAdmin && foto.getUser().equals(user)) || isSuperAdmin) {
	                fotoService.delete(foto);
	                String successMessages = "Foto eliminata definitivamente";
	   			 return "redirect:/admin/foto?successMessage=" + successMessages;
	                // Aggiungi il messaggio di successo come attributo flash
	              
	            }
	        }
	    }
	    return "redirect:/admin/foto";
	}


	// --------------------------------- FILTRI
	// --------------------------------------- //
	// Filtro Per Titolo:
	@PostMapping("/admin/foto/titolo")
	public String getFotoTitolo(@RequestParam(required = false) String titolo,
	                            @RequestParam(value = "categorie", required = false) Set<Integer> categorieSelezionate,
	                            @RequestParam(value = "page", defaultValue = "0") int page,
	                            Model model,
	                            Authentication authentication) {
	    String username = authentication.getName();
	    boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
	    model.addAttribute("isAdmin", isAdmin);
	    boolean isSuperAdmin = authentication.getAuthorities().stream()
	            .anyMatch(a -> a.getAuthority().equals("SUPERADMIN"));
	    model.addAttribute("isSuperAdmin", isSuperAdmin);
	    Optional<User> userOptional = userService.findByUsername(username);
	    if (userOptional.isPresent()) {
	        User user = userOptional.get();
	        Page<Foto> fotoPage = null;
	        List<Categoria> categoriaList = new ArrayList<>();

	        if (titolo != null && !titolo.isEmpty()) {
	            if (isSuperAdmin) {
	                if (categorieSelezionate == null || categorieSelezionate.isEmpty()) {
	                    fotoPage = fotoService.findByTitoloContaining(titolo, PageRequest.of(page, 15));
	                } else {
	                    List<Foto> fotoTemp = fotoService.findByTitoloAndCategorie(titolo, categorieSelezionate);
	                    List<Foto> filteredFoto = filterFotoByAllCategorie(fotoTemp, categorieSelezionate);
	                    int totalFilteredPhotos = filteredFoto.size(); // Calcolo del numero totale di foto filtrate
	                    if (totalFilteredPhotos > 0) {
	                        fotoPage = new PageImpl<>(filteredFoto, PageRequest.of(page, 15), totalFilteredPhotos);
	                    } else {
	                        fotoPage = new PageImpl<>(Collections.emptyList());
	                    }
	                }
	            } else {
	                if (categorieSelezionate == null || categorieSelezionate.isEmpty()) {
	                    fotoPage = fotoService.findByTitoloAndUser(titolo, user, PageRequest.of(page, 15));
	                } else {
	                    List<Foto> fotoTemp = fotoService.findByTitoloContainingAndUserAndCategorie(titolo, user, categorieSelezionate);
	                    List<Foto> filteredFoto = filterFotoByAllCategorie(fotoTemp, categorieSelezionate);
	                    int totalFilteredPhotos = filteredFoto.size(); // Calcolo del numero totale di foto filtrate
	                    if (totalFilteredPhotos > 0) {
	                        fotoPage = new PageImpl<>(filteredFoto, PageRequest.of(page, 15), totalFilteredPhotos);
	                    } else {
	                        fotoPage = new PageImpl<>(Collections.emptyList());
	                    }
	                }
	            }
	        } else {
	            if (isSuperAdmin) {
	                if (categorieSelezionate == null || categorieSelezionate.isEmpty()) {
	                    fotoPage = fotoService.findAll(PageRequest.of(page, 15));
	                } else {
	                    List<Foto> fotoTemp = fotoService.findAllByCategorie(categorieSelezionate);
	                    List<Foto> filteredFoto = filterFotoByAllCategorie(fotoTemp, categorieSelezionate);
	                    int totalFilteredPhotos = filteredFoto.size(); // Calcolo del numero totale di foto filtrate
	                    if (totalFilteredPhotos > 0) {
	                        fotoPage = new PageImpl<>(filteredFoto, PageRequest.of(page, 15), totalFilteredPhotos);
	                    } else {
	                        fotoPage = new PageImpl<>(Collections.emptyList());
	                    }
	                }
	            } else {
	                if (categorieSelezionate == null || categorieSelezionate.isEmpty()) {
	                    fotoPage = fotoService.findByUser(user, PageRequest.of(page, 15));
	                } else {
	                    List<Foto> fotoTemp = fotoService.findByUserAndCategorie(user, categorieSelezionate);
	                    List<Foto> filteredFoto = filterFotoByAllCategorie(fotoTemp, categorieSelezionate);
	                    int totalFilteredPhotos = filteredFoto.size(); // Calcolo del numero totale di foto filtrate
	                    if (totalFilteredPhotos > 0) {
	                        fotoPage = new PageImpl<>(filteredFoto, PageRequest.of(page, 15), totalFilteredPhotos);
	                    } else {
	                        fotoPage = new PageImpl<>(Collections.emptyList());
	                    }
	                }
	            }
	        }

	        if (isSuperAdmin) {
	            categoriaList = categoriaService.findAll();
	        } else {
	            categoriaList = categoriaService.findByUser(user);
	        }
	        System.out.println("Valore del titolo: " + titolo);
	        model.addAttribute("titoloValue", titolo); // Aggiunta del valore del titolo per mantenerlo nello stato
	        model.addAttribute("username", username);
	        model.addAttribute("fotos", fotoPage.getContent());
	        model.addAttribute("titoloValue", titolo);
	        model.addAttribute("categorie", categoriaList);
	        model.addAttribute("categorieSelezionate", categorieSelezionate);
	        model.addAttribute("currentPage", fotoPage.getNumber());
	        model.addAttribute("totalPages", fotoPage.getTotalPages());
	    }
	    return "Foto/index";
	}





	private List<Foto> filterFotoByAllCategorie(List<Foto> fotoList, Set<Integer> categorieSelezionate) {
	    List<Foto> filteredFoto = new ArrayList<>();

	    for (Foto foto : fotoList) {
	        // Verifica se la foto appartiene a tutte le categorie selezionate
	        boolean containsAllCategories = true;
	        for (Integer categoriaId : categorieSelezionate) {
	            boolean hasCategory = false;
	            for (Categoria categoria : foto.getCategorie()) {
	                if (categoria.getId()==categoriaId) {
	                    hasCategory = true;
	                    break;
	                }
	            }
	            if (!hasCategory) {
	                containsAllCategories = false;
	                break;
	            }
	        }

	        // Aggiungi la foto alla lista filtrata se appartiene a tutte le categorie selezionate
	        if (containsAllCategories) {
	            filteredFoto.add(foto);
	        }
	    }

	    return filteredFoto;
	}



}

/*
 * @Controller public class FotoController {
 * 
 * @Autowired private FotoService fotoService;
 * 
 * @Autowired private CategoriaService categoriaService; //
 * -------------------------------- INDEX --------------------------------------
 * //
 * 
 * @GetMapping("/admin/foto") public String index(Model model) {
 * Optional<List<Foto>> optionalFoto = Optional.of(fotoService.findAll()); if
 * (optionalFoto.isPresent()) { model.addAttribute("fotos", optionalFoto.get());
 * } else { model.addAttribute("message", "Non ci sono foto"); } return
 * "Foto/index"; } // --------------------------------- SHOW
 * --------------------------------------- //
 * 
 * @GetMapping("/admin/foto/{id}") public String getFoto(Model
 * model, @PathVariable("id") int id) { Foto foto = getFotoById(id);
 * List<Categoria> categorie = categoriaService.findAll();
 * model.addAttribute("foto", foto); model.addAttribute("categoria", categorie);
 * return "Foto/show"; } private Foto getFotoById(int id) { List<Foto> fotos =
 * fotoService.findAll(); Foto singolaFoto = null; for (Foto foto : fotos) if
 * (foto.getId() == id) singolaFoto = foto; return singolaFoto; } //
 * --------------------------------- CREATE
 * --------------------------------------- //
 * 
 * @GetMapping("/admin/foto/create") public String create(Model model) {
 * List<Categoria> categorie = categoriaService.findAll();
 * model.addAttribute("foto", new Foto()); model.addAttribute("categorie",
 * categorie); model.addAttribute("categorieSelezionate", new
 * HashSet<Integer>()); return "Foto/create"; }
 * 
 * @PostMapping("/admin/foto/store") public String store(Model
 * model, @Valid @ModelAttribute Foto foto, BindingResult bindingResult,
 * 
 * @RequestParam(value = "categorieSelezionate", required = false) Set<Integer>
 * categorieSelezionate) { System.out.println("Categorie selezionate: " +
 * categorieSelezionate); if (bindingResult.hasErrors()) { for (ObjectError err
 * : bindingResult.getAllErrors()) { System.err.println("errore: " +
 * err.getDefaultMessage()); } if (foto.getVisibile() == null) {
 * foto.setVisibile(false); } model.addAttribute("foto", foto);
 * model.addAttribute("errors", bindingResult); List<Categoria> categorie =
 * categoriaService.findAll(); model.addAttribute("categorie", categorie);
 * model.addAttribute("categorieSelezionate", categorieSelezionate); return
 * "Foto/create"; } if (categorieSelezionate != null) { List<Categoria>
 * categorie = categoriaService.findByIds(new
 * ArrayList<>(categorieSelezionate)); foto.setCategorie(categorie); } else {
 * foto.setCategorie(null); } fotoService.save(foto); return
 * "redirect:/admin/foto"; } // --------------------------------- EDIT
 * --------------------------------------- //
 * 
 * @GetMapping("/admin/foto/edit/{id}") public String edit(Model
 * model, @PathVariable int id) { Optional<Foto> fotoOpt =
 * fotoService.findById(id); List<Categoria> categorie =
 * categoriaService.findAll(); Foto foto = fotoOpt.get();
 * model.addAttribute("foto", foto); model.addAttribute("categorie", categorie);
 * Set<Integer> categorieSelezionate =
 * foto.getCategorie().stream().map(Categoria::getId).collect(Collectors.toSet()
 * ); model.addAttribute("categorieSelezionate", categorieSelezionate); return
 * "Foto/edit"; }
 * 
 * @PostMapping("/admin/foto/update/{id}") public String update( Model model,
 * 
 * @PathVariable int id,
 * 
 * @Valid @ModelAttribute("foto") Foto foto, BindingResult bindingResult,
 * 
 * @RequestParam(value = "categorieSelezionate", required = false) Set<Integer>
 * categorieSelezionate) {
 * 
 * if (bindingResult.hasErrors()) { for (ObjectError err :
 * bindingResult.getAllErrors()) { System.err.println("errore: " +
 * err.getDefaultMessage()); } model.addAttribute("errors", bindingResult);
 * List<Categoria> categorie = categoriaService.findAll();
 * model.addAttribute("categorie", categorie); if (categorieSelezionate != null)
 * { model.addAttribute("categorieSelezionate", categorieSelezionate); } return
 * "Foto/edit"; } Foto existingFoto = fotoService.getFotoById(id); if
 * (existingFoto == null) { return "redirect:/admin/foto"; }
 * existingFoto.setTitolo(foto.getTitolo());
 * existingFoto.setDescrizione(foto.getDescrizione());
 * existingFoto.setUrl(foto.getUrl());
 * existingFoto.setVisibile(foto.getVisibile()); if (categorieSelezionate !=
 * null) { List<Categoria> categorie = categoriaService.findByIds(new
 * ArrayList<>(categorieSelezionate)); existingFoto.setCategorie(categorie); }
 * else { existingFoto.setCategorie(new ArrayList<>()); }
 * fotoService.save(existingFoto); return "redirect:/admin/foto"; } //
 * --------------------------------- DELETE
 * --------------------------------------- //
 * 
 * @GetMapping("/admin/foto/delete/{id}") public String delete(@PathVariable int
 * id) { Optional<Foto> fotoOpt = fotoService.findById(id); Foto foto =
 * fotoOpt.get(); fotoService.delete(foto); return "redirect:/admin/foto"; } //
 * --------------------------------- FILTRI
 * --------------------------------------- // // Filtro Per Titolo:
 * 
 * @PostMapping("/admin/foto/titolo") public String
 * getFotoTitolo(@RequestParam(required = false) String titolo, Model model) {
 * List<Foto> foto = fotoService.findByTitolo(titolo);
 * model.addAttribute("fotos", foto); model.addAttribute("titolo", titolo);
 * System.out.println(titolo); return "Foto/index"; }
 * 
 * 
 * }
 */
