package com.rvbraga.pontus.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rvbraga.pontus.model.Registro;
import com.rvbraga.pontus.model.Usuario;
import com.rvbraga.pontus.service.RegistroService;
import com.rvbraga.pontus.service.UsuarioService;


@Controller
@RequestMapping("pontus/")
public class RegistroController {
	@Autowired
	private RegistroService service;
	@Autowired
	private UsuarioService usuarioService;
	
	private String usuarioLogado;
	
		
	@GetMapping("login")
	public String login() {		
		return "login.html";
	}
	
	
	private void loadUsuarioLogado() {
		Authentication autenticado = SecurityContextHolder.getContext().getAuthentication();
		if(!(autenticado instanceof AnonymousAuthenticationToken)) {
			this.usuarioLogado = autenticado.getName();		
		}
	}
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("")
	public String home(Model model) {
		Usuario usuario;
		
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		System.out.println("Usuario_Id: "+usuario.getId());
		
		return "home.html";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
	@GetMapping("marcacao")
	public String marcacao(Model model) {	
		Usuario usuario;		 
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		System.out.println("Usuario_Id: "+usuario.getId());
		return "marcacao.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/marcacao") 
	public String registrar(Model model,@RequestParam(value="usuarioId",required=true) UUID id, @RequestParam(value="descricao", required=true) String descricao)  {
		
		System.out.println("Id do Usuario a ser salvo: "+id);
		
		try {
		Usuario usuario = usuarioService.findById(id);
		System.out.println("Usuário recuperado: "+ usuario.getUsername());
		Registro registro = new Registro();
		registro.setDescricao(descricao);
		registro.setMarcacao(LocalDateTime.now());
		registro.setStatus("Criado");
		List<Registro> lista = usuario.getRegistros();
		if(lista.isEmpty()) lista = new ArrayList<Registro>();
		lista.add(registro);
		usuario.setRegistros(lista);
		registro.setUsuario(usuario);
		registro.setAutorizador(null);
		service.save(registro);
		}catch(InvalidDataAccessApiUsageException ee) {
			System.out.println("Erro ao recuperar usuário: "+ee.toString());
		}catch(Exception e) {
			System.out.println("Erro ao salvar registro: "+e.toString());
		}
		
		return registros();
		
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("registros")
	public String registros() {
		Usuario usuario;		 
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		
		return "registros.html";
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("gerencial")
	public String gerencial() {
		return "gerencial.html";
	}
	
	
}
