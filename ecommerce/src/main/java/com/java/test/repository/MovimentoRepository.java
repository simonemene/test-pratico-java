package com.java.test.repository;

import com.java.test.entity.MovimentoEntity;
import com.java.test.entity.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovimentoRepository extends JpaRepository<MovimentoEntity,Long> {

	boolean existsByOrdine_OrdineIdAndProdotto_ProductId(
			String idOrdine,
			String idProdotto
	);

	Optional<MovimentoEntity> findByOrdine_OrdineIdAndProdotto_ProductId(String id,String prodotto);

	@Query
			("""
					SELECT m.prodotto.productId
					FROM MovimentoEntity m
					WHERE m.ordine.ordineId = :ordine
					AND (m.flgAnnullo IS NULL OR m.flgAnnullo <> 'S')
					"""
			)
	List<String> elencoProdottiOrdine(@Param("ordine") String ordine);

}
