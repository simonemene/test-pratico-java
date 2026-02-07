package com.java.test.repository;

import com.java.test.entity.RuoloEntity;
import com.java.test.enums.RuoloEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RuoloRepository extends JpaRepository<RuoloEntity,Long> {

    Optional<RuoloEntity> findByRuolo(RuoloEnum ruolo);
}
