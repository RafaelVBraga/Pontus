package com.rvbraga.pontus.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
	@GetMapping("home")
	public String home(Model model) {
		Usuario usuario;
		
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		
		
		return "home.html";
	}
	
	
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
	@GetMapping("marcacao")
	public String marcacao(Model model) {	
		Usuario usuario;		 
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		
		return "marcacao.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("/marcacao") 
	public String registrar(Model model,@RequestParam(value="usuarioId",required=true) UUID id, @RequestParam(value="descricao", required=true) String descricao)  {
				
		try {
		Registro registro = service.prepararNovoRegistro(id, descricao);
		service.save(registro);
		}catch(Exception e) {
			System.out.println("Erro ao salvar registro: "+e.toString());
		}
		
		return registros(model);
		
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("registros")
	public String registros(Model model) {
		Usuario usuario;	 
		LocalDate localDate = LocalDate.now();
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		model.addAttribute("mes",localDate.getMonthValue());
		model.addAttribute("ano",localDate.getYear());
		return "registros.html";
	}
	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
	@PostMapping("registros")
	public String pesquisaRegistros(Model model,@RequestParam(value="usuarioId",required=true) UUID id, @RequestParam(value="mes", required=true) int mes, @RequestParam(value="ano", required=true)int ano) {
		model.addAttribute("usuarioId", id);
		model.addAttribute("mes",mes);
		model.addAttribute("ano",ano);
		model.addAttribute("registroMes", service.carregaRegistrosMes(id, mes, ano));
		return "registros";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("gerencial")
	public String gerencial(Model model) {
		Usuario usuario;	 
		loadUsuarioLogado();
		usuario = usuarioService.findByName(usuarioLogado);
		model.addAttribute("usuarioId", usuario.getId());
		return "gerencial.html";
	}
	
	
}
