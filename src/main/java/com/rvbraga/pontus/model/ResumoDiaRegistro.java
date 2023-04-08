package com.rvbraga.pontus.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;
@Data
public class ResumoDiaRegistro implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dia;
	private LocalDateTime entrada;
	private LocalDateTime saida;
	private LocalTime horaEntrada;
	private LocalTime horaSaida;
	private float tempoExcedente;
	private float tempoFaltante;
}
