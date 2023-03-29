package com.rvbraga.pontus.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvbraga.pontus.model.Registro;
import com.rvbraga.pontus.model.Usuario;
import com.rvbraga.pontus.repository.RegistroRepository;

@Service
public class RegistroService {
	@Autowired
	private RegistroRepository registroRepository;
	public List<Registro> findByUsuario(Usuario usr){
		return registroRepository.findByUsuario(usr);
	}
	public List<Registro> findRegistrosByUsuarioOnMonth(Usuario usr, String mes, String ano){
		Month mesBase = LocalDate.of(Integer.parseInt(ano),Integer.parseInt(mes), 1).getMonth();
		LocalDateTime ldt1 = LocalDateTime.of(Integer.parseInt(ano),Integer.parseInt(mes), 1, 0, 0);
		LocalDateTime ldt2 = LocalDateTime.of(Integer.parseInt(ano),Integer.parseInt(mes),mesBase.length(LocalDate.now().isLeapYear()) , 23, 59);
		return registroRepository.findByUsuarioAndMarcacaoAfterAndMarcacaoBeforeOrderByMarcacao(usr, ldt1, ldt2);		
	}
	public Registro save(Registro registro) {
		
		return registroRepository.save(registro);
	}
	
	public Boolean deleteRegistro(UUID id) {
		try {
			registroRepository.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
	}

}
