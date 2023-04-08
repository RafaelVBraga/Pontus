package com.rvbraga.pontus.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rvbraga.pontus.model.Registro;
import com.rvbraga.pontus.model.ResumoDiaRegistro;
import com.rvbraga.pontus.model.Usuario;
import com.rvbraga.pontus.repository.RegistroRepository;

@Service
public class RegistroService {
	@Autowired
	private RegistroRepository registroRepository;
	@Autowired
	private UsuarioService usuarioService;

	public List<Registro> findByUsuario(Usuario usr) {
		return registroRepository.findByUsuario(usr);
	}

	public List<Registro> findRegistrosByUsuarioOnMonth(Usuario usr, String mes, String ano) {
		Month mesBase = LocalDate.of(Integer.parseInt(ano), Integer.parseInt(mes), 1).getMonth();
		LocalDateTime ldt1 = LocalDateTime.of(Integer.parseInt(ano), Integer.parseInt(mes), 1, 0, 0);
		LocalDateTime ldt2 = LocalDateTime.of(Integer.parseInt(ano), Integer.parseInt(mes),
				mesBase.length(LocalDate.now().isLeapYear()), 23, 59);
		return registroRepository.findByUsuarioAndMarcacaoAfterAndMarcacaoBeforeOrderByMarcacao(usr, ldt1, ldt2);
	}

	public Registro save(Registro registro) {

		return registroRepository.save(registro);
	}

	public Boolean deleteRegistro(UUID id) {
		try {
			registroRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Registro prepararNovoRegistro(UUID id, String descricao) {
		Usuario usuario = usuarioService.findById(id);

		Registro registro = new Registro();
		registro.setDescricao(descricao);
		registro.setMarcacao(LocalDateTime.now());
		registro.setStatus("Criado");
		List<Registro> lista = usuario.getRegistros();
		if (lista.isEmpty())
			lista = new ArrayList<Registro>();
		lista.add(registro);
		usuario.setRegistros(lista);
		registro.setUsuario(usuario);
		registro.setAutorizador(null);
		return registro;
	}

	public List<ResumoDiaRegistro> carregaRegistrosMes(UUID id, int mes, int ano) {

		List<ResumoDiaRegistro> resumoRegistros = new ArrayList<ResumoDiaRegistro>();
		/* Variável utilizada para descobrir o tamanho do mês solicitado */
		LocalDate anoAtual = LocalDate.of(ano, mes, 1);
		LocalDateTime inicio = LocalDateTime.of(anoAtual, LocalTime.of(0, 0));
		LocalDateTime fim = LocalDateTime.of(LocalDate.of(ano, mes, anoAtual.getMonth().length(anoAtual.isLeapYear())),
				LocalTime.of(23, 59));
		List<Registro> registros = registroRepository.findByUsuarioAndMarcacaoAfterAndMarcacaoBeforeOrderByMarcacao(
				usuarioService.findById(id), inicio, fim);
		if (!registros.isEmpty())
			System.out.println("Número de registros encontrados:" + registros.size());
		for (int dia = 1; dia <= anoAtual.getMonth().length(anoAtual.isLeapYear()); dia++) {
			ResumoDiaRegistro resumo = new ResumoDiaRegistro();
			resumo.setDia(dia);
			for (Registro registro : registros) {
				if (registro.getMarcacao().getDayOfMonth() == dia) {
					if (registro.getDescricao().equalsIgnoreCase("Entrada")) {
						System.out.println("diaMarcação: " + registro.getMarcacao().getDayOfMonth() + " - dia: " + dia);
						resumo.setEntrada(registro.getMarcacao());
						resumo.setHoraEntrada(registro.getMarcacao().toLocalTime());
					}
					if (registro.getDescricao().equalsIgnoreCase("Saída")) {
						System.out.println("diaMarcação: " + registro.getMarcacao().getDayOfMonth() + " - dia: " + dia);
						resumo.setHoraSaida(registro.getMarcacao().toLocalTime());
						resumo.setSaida(registro.getMarcacao());
					}
				}

			}
			try {
				long tempo = resumo.getEntrada().until(resumo.getSaida(), ChronoUnit.MINUTES);
				if (tempo > 240)
					resumo.setTempoExcedente(tempo - 240);
				else
					resumo.setTempoFaltante(240 - tempo);
			} catch (Exception e) {
				System.out.println("Impossível calcular tempo!" + dia);
				System.out.print(e.toString());
			}
			resumoRegistros.add(resumo);
		}

		return resumoRegistros;
	}

}
