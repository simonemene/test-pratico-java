package com.java.test.repository;

import com.java.test.entity.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteRepository  extends JpaRepository<UtenteEntity,Long> {

	Optional<UtenteEntity> findByEmail(String email);

	Optional<UtenteEntity> findByUtenteId(String idPubblico);
}
