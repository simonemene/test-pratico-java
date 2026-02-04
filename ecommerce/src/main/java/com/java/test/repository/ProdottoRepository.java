package com.java.test.repository;

import com.java.test.entity.ProdottoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository  extends JpaRepository<ProdottoEntity,Long> {

	ProdottoEntity findByProductId(String productId);
}
