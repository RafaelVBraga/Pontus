package com.rvbraga.pontus.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvbraga.pontus.model.Usuario;
import com.rvbraga.pontus.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
		@Autowired
		private UsuarioRepository usuarioRepo;
		
		public Usuario findByName(String username) {
			return usuarioRepo.findByUsername(username).get();
		}		
		
		public Usuario findById(UUID id) {
			return usuarioRepo.findById(id).get();
		}
}
