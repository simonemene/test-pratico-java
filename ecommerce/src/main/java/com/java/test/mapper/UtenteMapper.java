package com.java.test.mapper;

import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.entity.UtenteEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtenteMapper {

	public UtenteResponseDto toDto(UtenteEntity entity)
	{
		return new UtenteResponseDto(entity.getNome(),entity.getCognome(),entity.getEmail(),entity.getCodiceFiscale(),entity.getUtenteId());
	}

	public UtenteEntity toEntity(UtenteRequestDto dto)
	{
		return new UtenteEntity(dto.email(),dto.nome(),dto.cognome(),dto.codiceFiscale(),dto.password());
	}

	public List<UtenteResponseDto> toListaDto(List<UtenteEntity> utenti)
	{
		return utenti.stream().map(this::toDto).toList();
	}
}
