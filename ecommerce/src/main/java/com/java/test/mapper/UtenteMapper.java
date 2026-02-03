package com.java.test.mapper;

import com.java.test.dto.UtenteDto;
import com.java.test.entity.UtenteEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtenteMapper {

	public UtenteDto toDto(UtenteEntity entity)
	{
		return new UtenteDto(entity.getNome(),entity.getCognome(),entity.getEta(),entity.getEmail(),entity.getCodiceFiscale());
	}

	public UtenteEntity toEntity(UtenteDto dto)
	{
		return UtenteEntity.builder()
				.email(dto.email())
				.codiceFiscale(dto.codiceFiscale())
				.nome(dto.nome())
				.cognome(dto.cognome())
				.eta(dto.eta())
				.build();
	}

	public List<UtenteDto> toListaDto(List<UtenteEntity> utenti)
	{
		return utenti.stream().map(this::toDto).toList();
	}
}
