package com.java.test.repository;

import com.java.test.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<StockEntity,Long> {
}
