package com.java.test.security;

import com.java.test.repository.OrdineRepository;
import com.java.test.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ControlloSicurezzaOrdineService{

	private final OrdineRepository ordineRepository;

	public boolean utenteProprietario(String idOrdine) {
		String utenteSistema = SecurityContextHolder.getContext().getAuthentication().getName();
		return ordineRepository.existsByOrdineIdAndUtente_Email(idOrdine,utenteSistema);
	}
}
