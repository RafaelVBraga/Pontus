package com.rvbraga.pontus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pontus/")
public class RegistroController {
	
	@GetMapping("home")
	public String home() {
		return "home.html";
	}
	@GetMapping("marcacao")
	public String marcacao() {
		return "marcacao.html";
	}
	@GetMapping("registros")
	public String registros() {
		return "registros.html";
	}
	@GetMapping("gerencial")
	public String gerencial() {
		return "gerencial.html";
	}
	
	
}
