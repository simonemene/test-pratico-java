package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdineRepository extends JpaRepository<OrdineEntity,Long> {

	Optional<OrdineEntity> findByOrdineId(String id);
}
