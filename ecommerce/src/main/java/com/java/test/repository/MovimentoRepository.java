package com.java.test.repository;

import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoRepository extends JpaRepository<MovimentoEntity,Long> {

	boolean existsOrdine_OrdineIdAndProdotto_ProductId(String idOrdine,String idProdotto);
}
