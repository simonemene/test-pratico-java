package com.java.test.mapper;

import com.java.test.dto.MovimentoResponseDto;
import com.java.test.dto.OrdineResponseDto;
import com.java.test.dto.OrdiniResponseDto;
import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrdineMapper {

	public OrdineResponseDto toDto(String idOrdine,List<MovimentoEntity> movimento)
	{
		List<MovimentoResponseDto> movimenti = movimento.stream().map(mov->new MovimentoResponseDto(mov.getProdotto().getNome(),
				mov.getQuantita())).toList();
		return new OrdineResponseDto(idOrdine,movimenti, movimento.getFirst().getOrdine().getUtente().getUtenteId());
	}

	public OrdiniResponseDto toListaDto(List<OrdineEntity> ordini)
	{
		List<OrdineResponseDto> ordineLista = new ArrayList<>();
		for(OrdineEntity ordine : ordini)
		{
			ordineLista.add(this.toDto(ordine.getOrdineId(),ordine.getMovimento().stream()
					.toList()));
		}
		return new OrdiniResponseDto(ordineLista);
	}
}
