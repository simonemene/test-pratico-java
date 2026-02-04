package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoRepository extends JpaRepository<OrdineEntity,Long> {
}
