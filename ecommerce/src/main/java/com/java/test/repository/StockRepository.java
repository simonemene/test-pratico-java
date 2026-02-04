package com.java.test.repository;

import com.java.test.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockEntity,Long> {

	Optional<StockEntity> findByProdotto_ProductId(String id);
}
