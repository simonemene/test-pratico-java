package com.java.test.mapper;

import com.java.test.dto.UtenteRequestDto;
import com.java.test.dto.UtenteResponseDto;
import com.java.test.entity.RuoloEntity;
import com.java.test.entity.UtenteEntity;
import com.java.test.enums.RuoloEnum;
import com.java.test.exception.RuoloException;
import com.java.test.repository.RuoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UtenteMapper {

	private final PasswordEncoder passwordEncoder;

	private final RuoloRepository ruoloRepository;

	public UtenteResponseDto toDto(UtenteEntity entity)
	{
		return new UtenteResponseDto(entity.getNome(),entity.getCognome(),entity.getEmail(),entity.getCodiceFiscale(),entity.getUtenteId());
	}

	public UtenteEntity toEntity(UtenteRequestDto dto)
	{
		RuoloEntity ruolo = ruoloRepository.findByRuolo(RuoloEnum.ROLE_USER).orElseThrow(
				()->new RuoloException("Il ruolo non esiste",RuoloEnum.ROLE_USER.name())
		);
		return new UtenteEntity(dto.email(),dto.nome(),dto.cognome(),dto.codiceFiscale(),passwordEncoder.encode(dto.password()),ruolo);
	}

	public List<UtenteResponseDto> toListaDto(List<UtenteEntity> utenti)
	{
		return utenti.stream().map(this::toDto).toList();
	}
}
