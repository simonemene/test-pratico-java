package com.java.test.repository;

import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovimentoRepository extends JpaRepository<MovimentoEntity,Long> {

	boolean existsByOrdine_OrdineIdAndProdotto_ProductId(
			String idOrdine,
			String idProdotto
	);

	Optional<MovimentoEntity> findByOrdine_OrdineIdAndProdotto_ProductId(String id,String prodotto);

}
