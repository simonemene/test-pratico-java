package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import com.java.test.enums.StatoOrdineEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdineRepository extends JpaRepository<OrdineEntity,Long> {

	Optional<OrdineEntity> findByOrdineId(String id);

	Optional<OrdineEntity> findByOrdineIdAndStatoOrdine(String id, StatoOrdineEnum stato);
}
