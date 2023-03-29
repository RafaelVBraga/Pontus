package com.rvbraga.pontus.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rvbraga.pontus.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID>{

}
