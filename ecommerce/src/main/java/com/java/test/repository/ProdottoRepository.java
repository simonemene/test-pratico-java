package com.java.test.repository;

import com.java.test.entity.ProdottoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdottoRepository  extends JpaRepository<ProdottoEntity,Long> {

	Optional<ProdottoEntity> findByProductId(String productId);

	Page<ProdottoEntity> findByFlgAnnullo(String annullo,Pageable pageable);

	Page<ProdottoEntity> findAll(Pageable pageable);
}
