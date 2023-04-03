package com.rvbraga.pontus.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rvbraga.pontus.model.Registro;
import com.rvbraga.pontus.model.Usuario;
import com.rvbraga.pontus.service.RegistroService;


@Controller
@RequestMapping("pontus/")
public class RegistroController {
	@Autowired
	private RegistroService service;
	private Usuario usuario;
		
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("home")
	public String home(Model model) {
		return "home.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("marcacao")
	public String marcacao(Model model) {
		
		return "marcacao.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/marcacao")
	public Registro registrar(Model model, @Validated@ModelAttribute("pesquisa") String descricao,  Errors errors) {
		Registro registro = new Registro();
		registro.setDescricao(descricao);
		registro.setMarcacao(LocalDateTime.now());
		registro.setStatus("Criado");
		registro.setUsuario(null);
		
		return service.save(null);
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("registros")
	public String registros() {
		return "registros.html";
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("gerencial")
	public String gerencial() {
		return "gerencial.html";
	}
	
	
}
