package com.java.test.repository;

import com.java.test.entity.UtenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository  extends JpaRepository<UtenteEntity,Long> {

	UtenteEntity findByEmail(String email);
}
