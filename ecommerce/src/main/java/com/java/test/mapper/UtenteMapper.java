package com.java.test.mapper;

import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtenteMapper {

	public UtenteDto toDto(UtenteEntity entity)
	{
		return new UtenteDto(entity.getNome(),entity.getCognome(),entity.getEmail(),entity.getCodiceFiscale());
	}

	public UtenteEntity toEntity(UtenteDto dto)
	{
		return new UtenteEntity(dto.email(),dto.nome(),dto.cognome(),dto.codiceFiscale());
	}

	public List<UtenteDto> toListaDto(List<UtenteEntity> utenti)
	{
		return utenti.stream().map(this::toDto).toList();
	}
}
