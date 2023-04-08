package com.rvbraga.pontus.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rvbraga.pontus.model.Registro;
import com.rvbraga.pontus.model.Usuario;

public interface RegistroRepository extends JpaRepository<Registro, UUID>{

	List<Registro> findByUsuario(Usuario usr);
	List<Registro> findByUsuarioAndMarcacaoAfterAndMarcacaoBeforeOrderByMarcacao(Usuario usr,LocalDateTime start, LocalDateTime end);
	
}
