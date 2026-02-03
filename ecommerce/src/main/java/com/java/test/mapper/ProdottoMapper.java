package com.java.test.mapper;

import com.java.test.dto.ProdottoListResponseDto;
import com.java.test.dto.ProdottoRequestDto;
import com.java.test.dto.ProdottoResponseDto;
import com.java.test.entity.ProdottoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdottoMapper {

	public ProdottoResponseDto toDto(ProdottoEntity entity)
	{
		return new ProdottoResponseDto(entity.getPrezzo(),entity.getNome(),entity.getProductId());
	}

	public ProdottoEntity toEntity(ProdottoRequestDto dto)
	{
		return new ProdottoEntity(dto.prezzo(),dto.nome());
	}

	public ProdottoListResponseDto toListaDto(List<ProdottoEntity> entity)
	{
		return new ProdottoListResponseDto(entity.stream().map(this::toDto).toList());
	}
}
