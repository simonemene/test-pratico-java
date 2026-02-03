package com.java.test.repository;

import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository  extends JpaRepository<OrdineEntity,Long> {
}
